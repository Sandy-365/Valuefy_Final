



# 🎵 Audio Transcriber App with AssemblyAI & Google Gemini Integration [**5 min Video Walkthrough**](https://drive.google.com/file/d/1o6M3QrhylcThJYZJmizkOCXRR3nxYzzI/view?usp=sharing)

This Android app allows users to transcribe audio files using the AssemblyAI API and view key points and ToDo items. The app is built with **Java** and uses **Android Studio** for development.

🎬 [**5 min Video Walkthrough**](https://drive.google.com/file/d/1o6M3QrhylcThJYZJmizkOCXRR3nxYzzI/view?usp=sharing)

---

## 📁 Features

- 🎧 **Pick Audio File from Gallery**
- 📝 **Transcribe Audio using AssemblyAI API**
- ✅ **Save & View Key Points**
- 📋 **Saved ToDo List**
- 🔗 **Integration with Google Gemini API**

---

## 🚀 Getting Started

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/Sandy-365/Valuefy_Project.git
cd Valuefy_Project
```

### 2️⃣ Open in Android Studio

1. Launch Android Studio.
2. Click on **"Open an existing project"**.
3. Select the cloned repository folder.
4. Let **Gradle** sync the project.

---

## 🔑 API Integration

### AssemblyAI API

1. Sign up at [AssemblyAI](https://www.assemblyai.com) and get your API key.
2. Replace the API key in **`MainActivity.java`**:

```java
private static final String API_KEY = "YOUR_ASSEMBLYAI_API_KEY";
```

---

## 📱 App Flow

1. **Home Screen**:  
   Tap **"Choose audio file from gallery"** to pick an audio file. The app transcribes the audio and displays the result.

2. **Extract Text**:  
   Click **"Extract"** to send the transcribed text to the **Google Gemini API**.

3. **Saved Key Points**:  
   Click **"Saved Key Points"** to view stored key points.

4. **Saved ToDo**:  
   Click **"Saved ToDo"** to check saved To-Do items.

---

## 📂 How It Works

1. 📂 **File Picker** → Select an audio file.  
2. 🛠️ **AssemblyAI API** → Uploads and transcribes the audio.  
3. 📝 **Transcription Display** → Shows the result on screen.  
4. 🌟 **Google Gemini API** → Extracts key points (*via Extract button*).  
5. 📋 **Saved Key Points & ToDo** → Navigate using buttons.

---

## 🛡️ Permissions

Add these permissions in **`AndroidManifest.xml`**:

```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.INTERNET"/>
```

---

## ⚡ Tech Stack

- Java ☕
- Android Studio 📱
- AssemblyAI API 🎤
- Google Gemini API 🔗
