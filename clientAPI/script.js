/**
 * This script file contains functions and event listeners for interacting with the backend API.
 * It includes functions for adding a profile, retrieving profiles, retrieving a profile by username,
 * updating a profile's username, and deleting a profile y id.
 * The script also includes event listeners for buttons and input fields to trigger the corresponding functions.
 */
const URI = "http://localhost:8080/api";
const getProfiles = document.getElementById("getProfiles");
const addProfileBtn = document.getElementById("addProfile");
const output = document.getElementById("output");
const header = document.getElementById("header");
const getUserPosts = document.getElementById("getUserPosts");
const updateUsername = document.getElementById("updateUsername");
const updateUsernameDiv = document.getElementById("updateUsernameDiv");
const deleteProfileDiv = document.getElementById("deleteProfileDiv");

window.onload = () => {
    fetchAllProfiles();
    // Event listener for the "Get Profiles" button
    // Retrieves all profiles from the API and displays them in the output div
    // Also hides the "Update Username" and "Delete Profile" divs
    getProfiles.addEventListener("click", function () {
        fetchAllProfiles();
    });

    // Event listener for the "Add Profile" button
    // Retrieves the username from the input field and adds a new profile with that username to the API
    addProfileBtn.addEventListener("click", function () {
        const usernameInput = document.getElementById("usernameInput");
        const username = usernameInput.value;

        if (!username) {
            alert("Please enter a username.");
            return;
        }
        addProfile(username);
    });
    
    // Event listener for the "Get User Posts" button
    getUserPosts.addEventListener("click", function () {
        const userInput = document.getElementById("userInput");
        const username = userInput.value;

        if (!username) {
            alert("Please enter a username.");
            return;
        }

        profileByUsername(username);
    });

    // Event listener for the "Update Username" button
    // Retrieves the old and new usernames from the input fields and updates the profile with the new username
    // Also updates the input field with the new username and retrieves the profile with the new username
    // If the new username already exists, an error message is displayed
    // If the old username does not exist, an error message is displayed
    // Sends the new username to the API to update the profile
    const updateUsernameBtn = document.getElementById("updateUsername");
    updateUsernameBtn.addEventListener("click", function () {
        const usernameInput = document.getElementById("updateUsernameInput");
        const newUsername = usernameInput.value;

        if (!newUsername) {
            alert("Please enter a new username.");
            return;
        }

        const userInput = document.getElementById("userInput");
        const username = userInput.value;

        if (!username) {
            alert("Please enter a username.");
            return;
        }

        updateProfile(username, newUsername);
    });

    // Event listener for the "Delete Profile" button
    // Retrieves the profile ID from the input field and deletes the profile with that ID
    // If the ID is empty, an error message is displayed
    // Id sent to function to delete profile
    // Also clears the input fields and displays a message in the header div and empties the output pre
    const deleteProfileBtn = document.getElementById("deleteProfile");
    deleteProfileBtn.addEventListener("click", function () {
        let profileId = parseInt(document.getElementById("deleteUsernameInput").value);
        if (!profileId) {
            alert("Please enter a valid profile ID.");
            return;
        }
        deleteProfile(profileId);
        document.getElementById("userInput").value = "";
        document.getElementById("deleteUsernameInput").value = "";
        header.innerText = "Profile Deleted";
        output.innerText = "";
    });
}
function fetchAllProfiles(){
    fetch(URI + "/profiles")
    .then(response => response.json())
    .then(json => {
        header.innerText = "All Profiles";
        output.innerHTML = generateProfileHTML(json);
        updateUsernameDiv.style.display = "none";
        deleteProfileDiv.style.display = "none";
    })
}
/**
 * Generates HTML to display a list of profiles or a single profile both with the list of postings
 * @param {Array} profiles - An array of profile objects.
 * @returns {string} - The HTML string to display the profiles.
 */
function generateProfileHTML(profile) {
    let html = '';
    if (Array.isArray(profile)) {
        profile.forEach(singleProfile => {
            html += generateSingleProfileHTML(singleProfile);
        });
    } else {
        html += generateSingleProfileHTML(profile);
    }
    return html;
}

function generateSingleProfileHTML(profile) {
    let html = `
        <div><strong>Profile ID:</strong> ${profile.profileId}</div>
        <div><strong>Username:</strong> ${profile.userName}</div>
    `;

    if (profile.postings && profile.postings.length > 0) {
        html += `<div><strong>Postings:</strong></div>`;
        profile.postings.forEach(post => {
            html += `
                <div class="post">
                    <strong>Posting ID:</strong> ${post.postingId}<br>
                    <strong>Posting Text:</strong><span class="posting-text">${post.postingText}</span>
                    <br>
                    <strong>Date:</strong> ${post.date}<br>
                    <strong>Time:</strong> ${post.time}<br>
                    <strong>Posted by:</strong> ${post.userName}<br>
                    <hr>
                </div>
            `;
        });
    }

    return html;
}


/**
 * Adds a new profile with the given username to the API.
 * Sends the new profile to the API to be added
 * If the username already exists, an error message is displayed
 * If the profile is added successfully, the new profile is displayed in the output div
 * @param {string} username - The username of the new profile.
 */
function addProfile(username) {
    let newProfile = {
        userName: username,
    };

    let postInfo = {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(newProfile)
    };

    fetch(URI + "/profiles", postInfo)
        .then(response => {
            if (response.ok) {
                return response.json();
            } else if (response.status === 409) {
                throw new Error("Username already exists.");
            } else {
                throw new Error("Error adding profile.");
            }
        }).then(data => {
            header.innerText = "New Profile Added";
            output.innerHTML = generateProfileHTML(data);

        }).catch(error => {
            console.error('Fetch error:', error);
            alert(error.message);
        });
}


/**
 * Retrieves a profile from the API based on the given username.
 * If the profile is not found, an error message is displayed
 * If the profile is found, the profile is displayed in the output div
 * Also displays the "Update Username" and "Delete Profile" divs
 * Sends the username to the API to retrieve the profile
 * @param {string} username - The username of the profile to retrieve.
 */
function profileByUsername(username) {
    fetch(URI + "/profileByUsername?username=" + username)
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error("Profile not found.");
            }
        })
        .then(profile => {
            if (!profile) {
                header.innerText = "Profile not found";
                output.innerText = "";
                updateUsernameDiv.style.display = "none";
                deleteProfileDiv.style.display = "none";
            } else {
                header.innerText = "Posts by " + username;
                output.innerHTML = ""; 
                output.innerHTML = generateProfileHTML(profile);                
                updateUsernameDiv.style.display = "block";
                deleteProfileDiv.style.display = "block";
            }
        })
        .catch(error => {
            console.error("Error fetching profile:", error);
            header.innerText = "Error";
            output.innerText = error.message;
            updateUsernameDiv.style.display = "none";
            deleteProfileDiv.style.display = "none";
        });
}


/**
 * Updates the username of a profile in the API.
 * If the new username already exists, an error message is displayed
 * If the profile is updated successfully, the new profile is displayed in the output div
 * Sends the old and new usernames to the API to update the profile
 * Also displays an error message if the new username already exists
 * Also displays an error message if there is an error updating the profile
 * @param {string} oldUsername - The current username of the profile.
 * @param {string} newUsername - The new username to update to.
 */
function updateProfile(oldUsername, newUsername) {
    const requestBody = {
        newUsername: newUsername,
    };

    fetch(URI + "/updateUsername?username=" + oldUsername, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(requestBody),
    })
        .then(response => {
            if (response.ok) {
                output.innerText = "Updating..."
                setTimeout(() => {
                    profileByUsername(newUsername);
                }, 1000); 
            } else if(response.status === 409) {
                throw new Error("Username already exists.");
            } else {
                throw new Error("Error updating profile.");
            }
        }).catch(error => {
            output.innerText = error.message;
        });
}

/**
 * Deletes a profile from the API based on the given ID.
 * If the profile is deleted successfully, a success message is displayed
 * If there is an error deleting the profile, an error message is displayed
 * Sends the profile ID to the API to delete the profile
 * @param {number} id - The ID of the profile to delete.
 */
function deleteProfile(id) {
    fetch(URI + "/profiles/" + id, {
        method: "DELETE",
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error("Error deleting profile.");
            }
        })
}

   