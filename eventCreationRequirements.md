# Virtual Event Platform: High-Level Requirements

## 1. Accounts and Security
Using Spring Security and MySQL.

### 1.1 User Accounts
Users should be able to:
- Create accounts with details like full name, email address, and password.
- Update their account details.
- Delete their own accounts.

### 1.2 Authentication
Implement mechanisms for:
- User registration (with email verification).
- User login.
- "Forgot password" functionality.

### 1.3 Authorization
Define and implement varying user roles with different access levels, such as:
- Guest
- Event attendee
- Event host
- Administrator

## 2. Event Management
Build event management functions using Spring Boot and MySQL.

### 2.1 Creating an Event
Enable users to create events with details including:
- Event title
- Description
- Date
- Time
- Duration
- Event image
- Registration deadlines

### 2.2 Event Listing
Provide a listing of upcoming and past events. Implement filters for:
- Event name
- Date
- Category
- Location

### 2.3 Event Detail
Each event should have a detailed page with additional information like:
- Event details
- Host details
- Registration function

## 3. Real-Time Interaction
Implement real-time features using WebSocket.

### 3.1 Chatrooms
Enable chatrooms for events so participants can communicate in real-time.

### 3.2 Notifications
Provide real-time notifications for event updates and other platform activities, including email notifications.

## 4. Evaluation and Reports
Create evaluation and reporting features using Spring Boot and MySQL.

### 4.1 User Feedback
After an event, attendees should be able to:
- Leave feedback on their experience.
- Rate the event.

### 4.2 Reports
Event hosts should be able to request reports detailing:
- Number of attendees registered
- Event ratings and feedback
- Engagement levels during the event

## 5. Inbuilt Video Conferencing
If possible, embed a video conferencing tool for conducting events within the platform.