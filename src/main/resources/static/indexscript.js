function fetchExample() {
    fetch('https://peoplegeneratorapi.me/api/person/')
        .then(response => response.json())
        .then(data => {
            // Populate personal info
            document.getElementById('name').textContent = data.name;
            document.getElementById('age').textContent = data.age;
            document.getElementById('job').textContent = data.job;
            document.getElementById('incomeUSD').textContent = data.incomeUSD;
            document.getElementById('creditScore').textContent = data.creditScore;
            document.getElementById('ccNumber').textContent = data.ccNumber;
            document.getElementById('married').textContent = data.married ? 'Yes' : 'No';
            document.getElementById('hasChildren').textContent = data.hasChildren ? 'Yes' : 'No';
            document.getElementById('height').textContent = data.height;
            document.getElementById('weight').textContent = data.weight;
            document.getElementById('eyeColor').textContent = data.eyeColor;
            document.getElementById('email').textContent = data.email;
            document.getElementById('gender').textContent = data.gender;
            document.getElementById('hasDegree').textContent = data.hasDegree ? 'Yes' : 'No';
            document.getElementById('bloodType').textContent = data.bloodType;
            document.getElementById('username').textContent = data.username;
            document.getElementById('politicalLeaning').textContent = data.politicalLeaning;
            document.getElementById('religion').textContent = data.religion;
            document.getElementById('doB').textContent = data.doB;
            document.getElementById('gpa').textContent = data.gpa;

            // Populate address info
            document.getElementById('streetAddress').textContent = data.address.streetAddress;
            document.getElementById('city').textContent = data.address.city;
            document.getElementById('state').textContent = data.address.state;
            document.getElementById('country').textContent = data.address.country;
            document.getElementById('zip').textContent = data.address.zip;
            document.getElementById('geonameId').textContent = data.address.geonameId;
            document.getElementById('phoneNumber').textContent = data.address.phoneNumber;
            document.getElementById('ipAddress').textContent = data.address.ipAddress;
            document.getElementById('countryCode').textContent = data.address.countryCode;

            // Store raw response for modal
            window.rawResponse = JSON.stringify(data, null, 2);
            // Display response-section after fetching data
            document.querySelector('.response-section').style.display = 'block';
        })
        .catch(error => console.error('Error fetching data:', error));
}

// Modal functionality
var modal = document.getElementById("raw-modal");
var showRawBtn = document.getElementById("show-raw");
var closeBtn = document.getElementsByClassName("close")[0];

// When the user clicks the button, open the modal
showRawBtn.addEventListener('click', function() {
    modal.style.display = "block";
    document.getElementById('raw-response').textContent = window.rawResponse || 'No data available.';
});

// When the user clicks on <span> (x), close the modal
closeBtn.addEventListener('click', function() {
    modal.style.display = "none";
});

// When the user clicks anywhere outside of the modal, close it
window.addEventListener('click', function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
});