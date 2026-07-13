# 📬 Postman API Documentation — Traffic Light System API

Base URL (local): `http://localhost:8080`
Base URL (deployed): `https://YOUR-APP-NAME.onrender.com`

---

## 🔧 How to Test in Postman

1. Open Postman → **New** → **HTTP Request**
2. Choose method (GET/POST)
3. Paste the URL (with query param if needed)
4. Click **Send** (no body needed for any of these endpoints — all use query params)

---

## 1️⃣ Get Current State

| | |
|---|---|
| **Method** | `GET` |
| **URL** | `/api/traffic-light/state?intersection=main` |
| **Query Param** | `intersection` (optional, defaults to `"main"`) |

**✅ Response — `200 OK`**
```json
{
  "name": "main",
  "currentColor": "RED",
  "lastChangedAt": "2026-07-13T10:00:00.123",
  "history": [
    "Initialized at RED"
  ]
}
```

---

## 2️⃣ Advance to Next State

| | |
|---|---|
| **Method** | `POST` |
| **URL** | `/api/traffic-light/next?intersection=main` |

**✅ Response — `200 OK`** (first call: RED → GREEN)
```json
{
  "name": "main",
  "currentColor": "GREEN",
  "lastChangedAt": "2026-07-13T10:00:20.456",
  "history": [
    "Initialized at RED",
    "RED -> GREEN at 2026-07-13T10:00:20.456"
  ]
}
```

> 🔁 Call this repeatedly to see the cycle: RED → GREEN → YELLOW → RED → GREEN...

---

## 3️⃣ Reset to RED

| | |
|---|---|
| **Method** | `POST` |
| **URL** | `/api/traffic-light/reset?intersection=main` |

**✅ Response — `200 OK`**
```json
{
  "name": "main",
  "currentColor": "RED",
  "lastChangedAt": "2026-07-13T10:05:00.789",
  "history": [
    "Initialized at RED",
    "RED -> GREEN at 2026-07-13T10:00:20.456",
    "GREEN -> YELLOW at 2026-07-13T10:00:35.111",
    "RESET -> RED at 2026-07-13T10:05:00.789"
  ]
}
```

---

## 4️⃣ Multiple Intersections (System-Scale Demo)

Try different `intersection` names — each is tracked completely independently:

```
GET  /api/traffic-light/state?intersection=MG-Road
POST /api/traffic-light/next?intersection=MG-Road

GET  /api/traffic-light/state?intersection=Ring-Road
POST /api/traffic-light/next?intersection=Ring-Road
```

`MG-Road` and `Ring-Road` will have completely independent states/histories.

---

## 5️⃣ Get All Tracked Intersections

| | |
|---|---|
| **Method** | `GET` |
| **URL** | `/api/traffic-light/all` |

**✅ Response — `200 OK`**
```json
{
  "main": {
    "name": "main",
    "currentColor": "GREEN",
    "lastChangedAt": "2026-07-13T10:00:20.456",
    "history": ["Initialized at RED", "RED -> GREEN at 2026-07-13T10:00:20.456"]
  },
  "MG-Road": {
    "name": "MG-Road",
    "currentColor": "RED",
    "lastChangedAt": "2026-07-13T10:06:00.000",
    "history": ["Initialized at RED"]
  }
}
```

---

## 6️⃣ Health Check

| | |
|---|---|
| **Method** | `GET` |
| **URL** | `/api/health` |

**✅ Response — `200 OK`**
```
UP ✅
```

---

## 🧪 Suggested Test Order (Demo Flow)

1. `GET /api/traffic-light/state` → confirm starts at RED
2. `POST /api/traffic-light/next` → confirm moves to GREEN
3. `POST /api/traffic-light/next` → confirm moves to YELLOW
4. `POST /api/traffic-light/next` → confirm cycles back to RED
5. `POST /api/traffic-light/reset` → confirm force-reset works even mid-cycle
6. `GET /api/traffic-light/state?intersection=Test2` → confirm a brand-new intersection auto-creates at RED
7. `GET /api/traffic-light/all` → confirm both `main` and `Test2` show up independently

---

## 📦 Importing as a Postman Collection

1. Postman → **Collections** → **New Collection** → name it `Traffic Light API`
2. Add each request above
3. Set **Collection Variable** `base_url` = `http://localhost:8080` (or Render URL) → use `{{base_url}}/api/traffic-light/state` etc.
