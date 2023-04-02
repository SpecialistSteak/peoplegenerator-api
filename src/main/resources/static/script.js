const button = document.querySelector('button');
const codeblock = document.querySelector('#RESPONSE');

button.addEventListener('click', async () => {
    const response = await fetch('https://peoplegeneratorapi.live/api/person/');
    const data = await response.json();
    codeblock.textContent = JSON.stringify(data, null, 2);
});