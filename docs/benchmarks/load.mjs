import http from 'node:http';
import fs from 'node:fs';
import {performance} from 'node:perf_hooks';
import {gunzipSync} from 'node:zlib';
const stages=JSON.parse(fs.readFileSync(process.argv[2],'utf8'));
const results=[];
const run=process.env.RUN_PREFIX||Date.now().toString(36);
function request(agent,path,identity,validate=false){
 const start=performance.now();
 return new Promise(resolve=>{
  const req=http.get({host:'127.0.0.1',port:Number(process.env.TARGET_PORT||8094),path,agent,headers:{'X-Forwarded-For':identity}},response=>{
   let bytes=0;const chunks=[];
   response.on('data',chunk=>{bytes+=chunk.length;if(validate)chunks.push(chunk);});
   response.on('end',()=>{
    const receivedMs=performance.now()-start;
    let invalid=null;
    if(validate&&response.statusCode===200){try{const raw=Buffer.concat(chunks);const body=JSON.parse(response.headers['content-encoding']==='gzip'?gunzipSync(raw):raw);if(Array.isArray(body)){if(body.length!==Number(path.split('/').pop())||body.some(person=>!person||!person.name||!person.address))invalid='Missing profile or wrong array length';}else if(!body.name||!body.address)invalid='Missing profile';}catch(error){invalid=error.message;}}
    resolve({status:response.statusCode,ms:receivedMs,bytes,invalid});
   });
   response.on('error',error=>resolve({status:'response-error',ms:performance.now()-start,bytes,error:error.message}));
  });
  req.setTimeout(60000,()=>req.destroy(new Error('60s timeout')));
  req.on('error',error=>resolve({status:'network-error',ms:performance.now()-start,bytes:0,error:error.message}));
 });
}
for(let index=0;index<stages.length;index++){
 const stage=stages[index];const agent=new http.Agent({keepAlive:true,maxSockets:stage.concurrency});
 const statuses={};const latencies=[];const invalid=[];let bytes=0,sequence=0;
 const start=performance.now(),until=start+(stage.seconds||0)*1000;
 const workers=Array.from({length:stage.concurrency},async(_,worker)=>{
  let count=0;
  while(stage.requestsPerWorker?count<stage.requestsPerWorker:performance.now()<until){
   const identifier=stage.singleIp?'shared':String(sequence++%Number(process.env.CLIENT_ID_POOL||65536));
   const response=await request(agent,stage.path,run+'-'+index+'-'+identifier,stage.validate||count===0);
   statuses[response.status]=(statuses[response.status]||0)+1;latencies.push(response.ms);bytes+=response.bytes;
   if(response.invalid)invalid.push(response.invalid);
   count++;
  }
 });
 await Promise.all(workers);const seconds=(performance.now()-start)/1000;agent.destroy();latencies.sort((a,b)=>a-b);
 const percentile=q=>latencies[Math.min(latencies.length-1,Math.floor(q*latencies.length))]||0;
 const histogram={};for(const latency of latencies){const bucket=Math.round(latency*10)/10;histogram[bucket]=(histogram[bucket]||0)+1;}
 const result={histogram,...stage,seconds,total:latencies.length,statuses,successfulPerSecond:(statuses[200]||0)/seconds,allPerSecond:latencies.length/seconds,p50:percentile(.5),p95:percentile(.95),p99:percentile(.99),max:latencies.at(-1),bytes,invalid};
 results.push(result);fs.writeFileSync(process.argv[3],JSON.stringify({run,results},null,2));
 console.log(JSON.stringify({...result,histogram:undefined}));
 await new Promise(resolve=>setTimeout(resolve,1000));
}
