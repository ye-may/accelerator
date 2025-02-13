## DaNNeS

Docker template for solution with Nuxt and Spring

---

## Components
- Load balancer: **Nginx (:80)**
- Front-end: **Nuxt (:3000)**
- Back-end: **Spring (:8080)**

---

**Nginx** listens port `80`  
All requests starting with `/api` are redirected to **api** container on port `8080` (without `/api` prefix)  
All the rest requests are redirected to **ui** container on port `3000`  
This way we can use back-end api from our front-end without CORS error

---
