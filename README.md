> ⚠️ **Project Status: In Development**
>
> This Notification Service is still a **work in progress**. Features are being implemented and refined as part of the Smart Wallet microservice architecture.

# 🔔 Notification Service

The **Notification Service** is a REST-based microservice responsible for managing and delivering user notifications within the **Smart Wallet** platform.

It centralizes notification-related functionality such as managing user notification preferences, sending notifications, storing notification history, retrying failed notifications, and allowing users to clear their visible notification history without permanently deleting data.

The service is designed to operate independently and communicate with the Smart Wallet Web Application.

---

## 🔧 Core Functionalities

### 1. Notification Preferences
- Users can enable or disable notifications.
- Contact information (e.g. email address) is stored for notification delivery.
- Preferences are handled using an **upsert** approach:
  - If preferences exist, they are updated.
  - If they do not exist, they are created.
- Preferences can also be retrieved by user ID.

### 2. Send Notifications
- Send notifications to users based on system events.
- Currently supports **EMAIL** notifications.
- Each notification is stored with its status (SUCCEEDED or FAILED).

### 3. View Notification History
- Users can retrieve their notification history.
- Only notifications that are not cleared by the user are returned.

### 4. Retry Failed Notifications
- Previously failed notifications can be retried.
- Allows recovery from temporary delivery issues.

### 5. Clear Notification History
- Users can clear their notification history from their view.
- Notifications are **not deleted** from the database.
- Cleared notifications are simply excluded from user-facing results.
- History can be cleared entirely or filtered by notification type.

---

## 🔗 Integration with Smart Wallet

This service is consumed by the **Smart Wallet Web Application**.

The main application communicates with the Notification Service to:
- Manage user notification preferences
- Trigger notifications for events such as payments, transfers and subscriptions
- Retrieve notification history

Smart Wallet repository:
https://github.com/AlexGadjinski/Smart-Wallet-Web-Application

---

## 🗄 Data Handling

- Notification records are always preserved in the database.
- Clearing notifications does not remove data.
- Failed notifications remain available for retries.
- This approach ensures data integrity, traceability, and future extensibility.

---

## 📁 Tech Stack

- **Java + Spring Boot**
- **REST API**
- **MySQL** (persistence)
- **Maven** (project management)

---

## ⚙️ Running Locally / Setup

This service uses a **Gmail account to send notifications** to users. To configure it locally, you need to set the following environment variables in your IDE via **Edit Configurations**:

- `EMAIL_USERNAME` → the Gmail address used to send notifications
- `EMAIL_PASSWORD` → the Gmail **App Password** for that account

### Gmail App Password

To send emails from Gmail, you must generate an **App Password** for the sender account:

1. Go to **Manage your Google Account**.
2. Search for **App passwords**.
3. Create a new app password.
4. Click **Generate**.
5. Copy the 16-character password.
6. Use this password as the value of `EMAIL_PASSWORD` in your IDE’s environment variables.

> ⚠️ This Gmail account will be used to **send all notifications**. Do not use your personal Gmail password—only the generated App Password will work.

---

## 🧪 Usage

- The service runs independently from the Smart Wallet Web Application.
- Smart Wallet communicates with this service.
- Notifications are triggered as part of user actions in the main application.
