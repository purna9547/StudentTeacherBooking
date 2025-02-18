function sendMessage() {
    let userInput = document.getElementById("user-input").value;
    if (userInput.trim() === "") return;

    let chatBox = document.getElementById("chat-box");

    // Append user message to chat
    let userMessage = document.createElement("div");
    userMessage.classList.add("user-message");
    userMessage.innerText = userInput;
    chatBox.appendChild(userMessage);

    // Clear input field
    document.getElementById("user-input").value = "";

    // Send request to backend
    fetch("/student/chatapi", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ message: userInput })
    })
        .then(response => response.json())
        .then(data => {
            let botMessage = document.createElement("div");
            console.log(data.response);
            botMessage.classList.add("bot-message");
            botMessage.innerText = data.response;
            chatBox.appendChild(botMessage);

            // Auto-scroll to the latest message
            chatBox.scrollTop = chatBox.scrollHeight;
        })
        .catch(error => {
            console.error("Error:", error);
        });
}

document.getElementById("user-input").addEventListener("keypress", function(event) {
    if (event.key === "Enter") {
        event.preventDefault(); // Prevents form submission
        sendMessage(); // Calls the sendMessage function
    }
});



caches.set("chat", "chat");

