# hobby-log
HobbyLog is a JavaFX application designed to help users document and track progress on their hobbies. Built with an intuitive and clean user interface, HobbyLog provides functionality to create, update, and organize logs, track materials, add media (photos and videos), and maintain a personal feed of activities. The application supports media storage using AWS S3 and implements a persistent database using SQLite. HobbyLog is ideal for hobbyists looking to manage and share their projects in a structured, interactive way.

## Features
User Profile: Each user has a customizable profile, including profile photos stored securely in AWS S3.
Project Logs: Create logs for various hobbies, document progress, and add media.
To-Do Lists: Integrate to-do lists with each log to keep track of pending tasks.
Material Tracking: Keep a detailed list of materials needed for each hobby project, including quantity, cost, and total expense calculation.
Media Management: Upload images and videos directly to AWS S3 and display them in a dynamic feed.
Activity Feed: A personalized feed showcasing updates and events related to each hobby log.
Explore Page: View blogs and other shared logs from different users, with tags and descriptions.
Interactive Slideshow: Display media from each log in an interactive slideshow view.

## Project Structure
The application follows a Model-View-Controller (MVC) architecture:
Model: Contains classes for handling data and business logic, including DAOs for database operations and models for representing entities.
View: FXML layouts and JavaFX components for the user interface.
Controller: Manages interactions between the Model and View, handling user actions and updating the UI.

## Prerequisites
Java 17 or higher
Maven for dependency management
AWS S3 Account: Set up an S3 bucket and obtain AWS credentials (Access Key and Secret Key).

## Usage
User Login: Log in to access personalized projects and profiles.
Create a New Log: Start a new hobby log, add materials, tasks, and media.
Add Media: Upload photos and videos associated with each log; files are stored in AWS S3.
Track Progress: Use the to-do list and materials tracker to monitor and update project status.
Explore Blogs: Check out shared projects and blogs from other users on the Explore page.
Profile Customization: Update profile information, including profile photo, to personalize your HobbyLog account.

## Dependencies
JavaFX: For building the user interface.
SQLite: Local database management for storing user data and logs.
AWS SDK: For integrating AWS S3 storage.
Maven: Dependency and build management.
