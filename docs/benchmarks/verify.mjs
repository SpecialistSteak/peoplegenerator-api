import fs from 'node:fs';
import assert from 'node:assert/strict';
const base=process.env.TARGET_URL || 'http://127.0.0.1:8096';
const codes=new Map();
for(const line of fs.readFileSync(new URL('../../src/main/resources/org/specialiststeak/peoplegenerator/person/utils/COUNTRYNAME_COUNTRYCODE.csv', import.meta.url),'utf8').trim().split('\n')){
 const [country,code]=line.split(',').slice(0,2).map(x=>x.replaceAll('"',''));
 if(!codes.has(country))codes.set(country,new Set());codes.get(country).add(code);
}
let sequence=0;
async function request(path,identity){
 const started=performance.now();const response=await fetch(base+path,{headers:{'X-Forwarded-For':identity||'postfix-'+Date.now()+'-'+sequence++},signal:AbortSignal.timeout(60000)});
 const text=await response.text();
 const result={status:response.status,milliseconds:performance.now()-started,retryAfter:response.headers.get('retry-after')};
 if(response.status===200){
  const body=JSON.parse(text),people=Array.isArray(body)?body:[body];
  if(Array.isArray(body))assert.equal(people.length,Number(path.split('/').pop()));
  let mismatches=0;
  for(const person of people){assert.equal(Object.keys(person).length,21);assert.equal(Object.keys(person.address).length,9);if(!codes.get(person.address.country)?.has(person.address.countryCode))mismatches++;}
  result.people=people.length;result.countryMismatches=mismatches;assert.equal(mismatches,0);
 }
 return result;
}
for(let attempt=0;attempt<60;attempt++){
 try{await fetch(base+'/');break;}catch(error){if(attempt===59)throw error;await new Promise(resolve=>setTimeout(resolve,250));}
}
const results={single:await request('/api/person/'),batch:await request('/api/person/50000')};
results.overload=await Promise.all(Array.from({length:32},()=>request('/api/person/50000')));
const accepted=results.overload.filter(x=>x.status===200),rejected=results.overload.filter(x=>x.status===503);
assert.equal(accepted.length,4);assert.equal(rejected.length,28);assert.ok(rejected.every(x=>x.retryAfter==='10'));
results.sameClient=await Promise.all(Array.from({length:16},()=>request('/api/person/1','same-client-'+process.pid)));
assert.equal(results.sameClient.filter(x=>x.status===200).length,1);assert.equal(results.sameClient.filter(x=>x.status===429).length,15);
results.recovery=await request('/api/person/1000');assert.equal(results.recovery.status,200);
results.summary={accepted:accepted.length,rejected:rejected.length,validatedPeople:[results.single,results.batch,...results.overload,...results.sameClient,results.recovery].reduce((sum,x)=>sum+(x.people||0),0),countryMismatches:0};
fs.writeFileSync('bulk-verification.json',JSON.stringify(results,null,2));console.log(JSON.stringify(results.summary));
