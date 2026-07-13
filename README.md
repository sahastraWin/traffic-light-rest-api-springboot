# 🚦 Traffic Light System REST API — Spring Boot

> A **state-machine based REST API** simulating a real-world traffic light system. This project is built specifically to cover two things Bajaj Finserv has asked before: **(1)** deep understanding of **GET vs POST vs PUT**, and **(2)** a **simple system design** question — *"design a traffic light system."* 🎯

---

## 📑 Table of Contents

- [🧠 Part A: REST Method Theory (GET vs POST vs PUT vs DELETE)](#-part-a-rest-method-theory-get-vs-post-vs-put-vs-delete)
- [🧠 Part B: Traffic Light System Design Theory](#-part-b-traffic-light-system-design-theory)
- [🧠 What This Project Does](#-what-this-project-does)
- [🏗️ Architecture](#️-architecture)
- [💻 Run Locally in IntelliJ IDEA](#-run-locally-in-intellij-idea)
- [🔀 Push to GitHub](#-push-to-github)
- [☁️ Deploy on Render](#️-deploy-on-render)
- [📡 API Flow](#-api-flow)
- [📬 Postman Testing](#-postman-testing)
---

## 🧠 Part A: REST Method Theory (GET vs POST vs PUT vs DELETE)

### 🔹 GET — "Give me data"
- **Purpose:** Retrieve/read a resource
- **Body:** No request body
- **Idempotent:** ✅ Yes (calling it 100 times doesn't change anything)
- **Safe:** ✅ Yes (never modifies server state)
- **Cacheable:** ✅ Yes
- **Example:** `GET /api/traffic-light/state` → returns current light color

### 🔹 POST — "Create something / trigger an action"
- **Purpose:** Create a new resource, OR trigger a non-idempotent action
- **Body:** ✅ Yes, contains the data
- **Idempotent:** ❌ No (calling `POST /api/traffic-light/next` twice moves the light TWO steps forward, not one)
- **Safe:** ❌ No (changes server state)
- **Example:** `POST /api/traffic-light/next` → advances the light to its next color

### 🔹 PUT — "Replace this entire resource"
- **Purpose:** Update/replace an existing resource completely, at a known URI
- **Body:** ✅ Yes, the full new representation
- **Idempotent:** ✅ Yes (calling `PUT /students/5` with the same body 5 times gives the same final state)
- **Example:** `PUT /api/students/5` with a full student JSON → replaces student 5 entirely

### 🔹 PATCH — "Update part of this resource"
- **Purpose:** Partial update — only the fields you send are changed
- **Idempotent:** ❌ Technically not guaranteed (though often is in practice)
- **Example:** `PATCH /api/students/5` with `{"age": 23}` → only updates age

### 🔹 DELETE — "Remove this resource"
- **Purpose:** Delete a resource
- **Idempotent:** ✅ Yes (deleting the same resource twice — second time it's already gone, still ends in "resource doesn't exist")
- **Example:** `DELETE /api/students/5`

### 🎯 THE most common interview question here:
**"Why is POST not idempotent but PUT is?"**
> Because POST typically means "create a new thing" — calling it N times creates N things (side effects multiply). PUT means "make this URI look exactly like this" — calling it N times still results in the exact same end state, so nothing new accumulates.

**In THIS project specifically:**
- `GET /api/traffic-light/state` → safe, idempotent, just reads
- `POST /api/traffic-light/next` → NOT idempotent — each call moves the state machine forward one step (RED→GREEN→YELLOW→RED...). This is intentionally a great example to explain non-idempotent POST usage that ISN'T about "creating a resource" — it's about triggering a state transition.
- `POST /api/traffic-light/reset` → interestingly, this one behaves idempotently in effect (light always ends at RED no matter how many times you call it) even though it's a POST — good talking point to show you understand idempotency is about *effect*, not the HTTP verb alone.

---

## 🧠 Part B: Traffic Light System Design Theory

When asked *"design a traffic light system"* in an interview, they're testing **basic state-machine / logical design thinking**, not distributed systems depth. Structure your answer like this:

### Step 1: Identify the states
A traffic light has exactly 3 states: 🔴 RED → 🟢 GREEN → 🟡 YELLOW → back to 🔴 RED. This is a **finite state machine (FSM)** — a fixed, cyclical set of states with defined transitions.

### Step 2: Identify the transition rules
- RED → GREEN (never RED → YELLOW directly — real traffic lights don't skip states)
- GREEN → YELLOW
- YELLOW → RED
- Each state has a fixed duration (RED=20s, GREEN=15s, YELLOW=5s in this project)

### Step 3: Identify the triggers
- **Time-based (automatic):** in the real world, a timer advances the state automatically
- **Event-based (manual override):** emergency vehicle → force RED; sensor detects no cars → skip ahead

### Step 4: Think about scale — multiple intersections
A real city has thousands of independent traffic lights. Design decision: **each intersection is an independent state machine**, tracked separately (in this project, via a `Map<String, Intersection>` keyed by intersection name). This means intersection "MG-Road" and "Ring-Road" can be in completely different states simultaneously, and don't affect each other.

### Step 5: Think about concurrency
Multiple requests could hit different intersections at the same time → use a **thread-safe data structure** (`ConcurrentHashMap` in this project) so simultaneous updates don't corrupt state.

### 🎯 How to answer in the interview (spoken summary):
> *"I'd model it as a finite state machine with three states — RED, GREEN, YELLOW — and fixed transition rules where each state can only move to one specific next state. Each intersection would be its own independent instance of this state machine, tracked in a map keyed by intersection ID, so the system scales to many intersections. State transitions would primarily be time-driven via a scheduler, with an event-driven override path for emergencies. I'd expose REST endpoints to read the current state (GET) and trigger transitions (POST), and use a thread-safe map since multiple intersections could be updated concurrently."*

---

## 🧠 What This Project Does

A REST API simulating **one or more traffic light intersections**:

- 🔴 Every new intersection starts at RED (safety default)
- ➡️ You can advance it: RED → GREEN → GREEN → YELLOW → RED (cyclic)
- 🔁 You can reset it back to RED anytime (emergency override)
- 📜 Each intersection keeps a history log of all its transitions
- 🗺️ Supports **multiple named intersections** simultaneously (e.g. `MG-Road`, `Ring-Road`) to demonstrate system-scale thinking

---

## 🏗️ Architecture

```
Client (Postman)
      ↓
TrafficLightController   → REST endpoints (GET/POST)
      ↓
TrafficLightService        → Manages a Map<String, Intersection>, thread-safe
      ↓
Intersection (model)        → Holds current LightColor + transition history
      ↓
LightColor (enum)            → RED/GREEN/YELLOW + next() state machine logic
```

---

## 🛠️ Tech Stack

☕ Java 17 · 🍃 Spring Boot 3.3.2 · 📦 Maven · 🐳 Docker · ☁️ Render

---

## 📁 Project Structure

```
traffic-light-api/
├── src/main/java/com/bajaj/trafficlight/
│   ├── TrafficLightApplication.java
│   ├── controller/
│   │   ├── TrafficLightController.java
│   │   └── RootController.java
│   ├── service/
│   │   └── TrafficLightService.java
│   └── model/
│       ├── LightColor.java        ← enum + state machine transition logic
│       └── Intersection.java      ← per-intersection state + history
├── src/main/resources/application.properties
├── Dockerfile
├── pom.xml
└── POSTMAN_API_DOCUMENTATION.md
```

---

## 💻 Run Locally in IntelliJ IDEA

1. **Install Java 17**: https://adoptium.net/ (skip if already installed)
2. **Install IntelliJ IDEA Community**: https://www.jetbrains.com/idea/download/
3. Unzip `traffic-light-api.zip` → IntelliJ → **Open** → select folder → **"Load Maven Project"**
4. Open `TrafficLightApplication.java` → click green ▶️ Run
5. Wait for `Tomcat started on port(s): 8080`
6. Test in browser: `http://localhost:8080/`

---

## 🔀 Push to GitHub

```bash
git init
git add .
git commit -m "Initial commit - Traffic Light System REST API"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/traffic-light-api.git
git push -u origin main
```

---

## ☁️ Deploy on Render

1. https://render.com → Sign up with GitHub
2. **New +** → **Web Service** → connect `traffic-light-api` repo
3. Render auto-detects the `Dockerfile` → **Instance Type: Free** → **Create Web Service**
4. Wait for build (3-5 min) → live URL like:
   ```
   https://traffic-light-api-xxxx.onrender.com
   ```

---

## 📡 API Flow

```
1. GET  /api/traffic-light/state?intersection=main   → current color        → 200 OK
2. POST /api/traffic-light/next?intersection=main     → advance one step     → 200 OK
3. POST /api/traffic-light/reset?intersection=main     → force back to RED   → 200 OK
4. GET  /api/traffic-light/all                          → all intersections   → 200 OK
```

---

## 📬 Postman Testing

See the included **`POSTMAN_API_DOCUMENTATION.md`** file in this repo for:
- ✅ All endpoints with exact URLs
- ✅ Sample JSON request bodies
- ✅ Expected responses
- ✅ Status codes for success & error cases
- ✅ How to import into Postman as a collection

---

Made with ☕ + 🍃 Spring Boot | Bajaj Finserv API Round Prep
