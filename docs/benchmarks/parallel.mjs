import fs from 'node:fs';
import {spawn} from 'node:child_process';
import {performance} from 'node:perf_hooks';
const stages=JSON.parse(fs.readFileSync(process.argv[2],'utf8'));const results=[];
const run=Date.now().toString(36);
for(let index=0;index<stages.length;index++){
 const stage=stages[index];const workers=Math.min(4,stage.concurrency);const start=performance.now();
 const parts=await Promise.all(Array.from({length:workers},(_,worker)=>new Promise((resolve,reject)=>{
  const part={...stage,concurrency:Math.floor(stage.concurrency/workers)+(worker<stage.concurrency%workers?1:0)};
  const input=`part-${index}-${worker}.json`,output=`part-${index}-${worker}-result.json`;
  fs.writeFileSync(input,JSON.stringify([part]));
  const child=spawn(process.execPath,['load.mjs',input,output],{env:{...process.env,RUN_PREFIX:run+'-'+index+'-'+worker},stdio:['ignore','ignore','pipe']});let errors='';child.stderr.on('data',x=>errors+=x);
  child.on('exit',code=>code?reject(new Error(errors)):resolve(JSON.parse(fs.readFileSync(output,'utf8')).results[0]));
 })));
 const histogram={},statuses={};let total=0,bytes=0,invalid=[];
 for(const part of parts){total+=part.total;bytes+=part.bytes;invalid.push(...part.invalid);for(const [key,count] of Object.entries(part.histogram))histogram[key]=(histogram[key]||0)+count;for(const [key,count] of Object.entries(part.statuses))statuses[key]=(statuses[key]||0)+count;}
 const bins=Object.entries(histogram).map(([key,count])=>[Number(key),count]).sort((a,b)=>a[0]-b[0]);
 function percentile(q){let sum=0;for(const [value,count] of bins){sum+=count;if(sum>=total*q)return value;}return 0;}
 const seconds=Math.max(...parts.map(part=>part.seconds));
 const result={...stage,startedAt:Date.now()-(performance.now()-start),endedAt:Date.now(),seconds,total,statuses,successfulPerSecond:(statuses[200]||0)/seconds,profilesPerSecond:(statuses[200]||0)/seconds*(stage.batchSize||1),p50:percentile(.5),p95:percentile(.95),p99:percentile(.99),max:Math.max(...parts.map(part=>part.max)),bytes,invalid};
 results.push(result);fs.writeFileSync(process.argv[3],JSON.stringify({run,results},null,2));console.log(JSON.stringify(result));
}
