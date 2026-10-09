const generateButton = document.getElementById('generate-person');
const requestStatus = document.getElementById('request-status');
const responseSection = document.querySelector('.response-section');
const rawModal = document.getElementById('raw-modal');
const rawResponse = document.getElementById('raw-response');
const copyStatus = document.getElementById('copy-status');
let responseText = '';

async function fetchExample() {
    generateButton.disabled = true;
    requestStatus.textContent = 'Requesting a person...';
    requestStatus.classList.remove('error');
    const started = performance.now();
    try {
        const response = await fetch('/api/person/', { signal: AbortSignal.timeout(15000) });
        if (!response.ok) {
            throw new Error(response.status === 429
                ? 'Too many requests. Wait a moment and try again.'
                : `The API returned HTTP ${response.status}. Try again in a moment.`);
        }
        const person = await response.json();
        for (const [field, value] of Object.entries({ ...person, ...person.address })) {
            const target = document.getElementById(field);
            if (target) {
                target.textContent = value === null ? 'None' : typeof value === 'boolean' ? (value ? 'Yes' : 'No') : String(value);
            }
        }
        responseText = JSON.stringify(person, null, 2);
        rawResponse.textContent = responseText;
        responseSection.style.display = 'block';
        requestStatus.textContent = `${response.status} OK · ${Math.round(performance.now() - started)} ms · application/json`;
        generateButton.textContent = 'Generate another person';
    } catch (error) {
        requestStatus.textContent = error.name === 'TimeoutError'
            ? 'The request timed out. Try again in a moment.'
            : error instanceof TypeError ? 'Could not reach the API. Try again in a moment.' : error.message;
        requestStatus.classList.add('error');
    } finally {
        generateButton.disabled = false;
    }
}

generateButton.addEventListener('click', fetchExample);
document.getElementById('show-raw').addEventListener('click', () => {
    copyStatus.textContent = '';
    rawModal.showModal();
    rawResponse.scrollTop = 0;
});
document.getElementById('close-json').addEventListener('click', () => rawModal.close());
rawModal.addEventListener('click', event => {
    if (event.target === rawModal) {
        const bounds = rawModal.getBoundingClientRect();
        if (event.clientX < bounds.left || event.clientX > bounds.right || event.clientY < bounds.top || event.clientY > bounds.bottom) rawModal.close();
    }
});
document.getElementById('copy-json').addEventListener('click', async () => {
    try {
        await navigator.clipboard.writeText(responseText);
        copyStatus.textContent = 'Copied to clipboard.';
    } catch {
        copyStatus.textContent = 'Could not copy. You can select and copy the JSON below.';
    }
});
