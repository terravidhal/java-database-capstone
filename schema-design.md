# Schema Design for Smart Clinic Management System

## MySQL Database Design

### Table: patients
- id: INT, Primary Key, Auto Increment
- first_name: VARCHAR(50), NOT NULL
- last_name: VARCHAR(50), NOT NULL
- email: VARCHAR(100), UNIQUE, NOT NULL
- phone: VARCHAR(20), NOT NULL
- date_of_birth: DATE
- created_at: DATETIME, NOT NULL
- updated_at: DATETIME, NOT NULL

### Table: doctors
- id: INT, Primary Key, Auto Increment
- first_name: VARCHAR(50), NOT NULL
- last_name: VARCHAR(50), NOT NULL
- email: VARCHAR(100), UNIQUE, NOT NULL
- phone: VARCHAR(20), NOT NULL
- specialization: VARCHAR(100)
- available_from: TIME
- available_to: TIME
- created_at: DATETIME, NOT NULL
- updated_at: DATETIME, NOT NULL

### Table: appointments
- id: INT, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key → doctors(id), NOT NULL
- patient_id: INT, Foreign Key → patients(id), NOT NULL
- appointment_time: DATETIME, NOT NULL
- status: INT (0 = Scheduled, 1 = Completed, 2 = Cancelled)
- created_at: DATETIME, NOT NULL
- updated_at: DATETIME, NOT NULL

### Table: admin
- id: INT, Primary Key, Auto Increment
- username: VARCHAR(50), UNIQUE, NOT NULL
- email: VARCHAR(100), UNIQUE, NOT NULL
- password_hash: VARCHAR(255), NOT NULL
- role: VARCHAR(50), NOT NULL
- created_at: DATETIME, NOT NULL
- updated_at: DATETIME, NOT NULL

### Table: clinic_locations
- id: INT, Primary Key, Auto Increment
- name: VARCHAR(100), NOT NULL
- address: VARCHAR(255), NOT NULL
- phone: VARCHAR(20)
- created_at: DATETIME, NOT NULL
- updated_at: DATETIME, NOT NULL

### Table: payments
- id: INT, Primary Key, Auto Increment
- appointment_id: INT, Foreign Key → appointments(id), NOT NULL
- patient_id: INT, Foreign Key → patients(id), NOT NULL
- amount: DECIMAL(10,2), NOT NULL
- payment_date: DATETIME, NOT NULL
- status: VARCHAR(50) (Paid, Pending, Failed)
- created_at: DATETIME, NOT NULL
- updated_at: DATETIME, NOT NULL

### Table: messages
- id: INT, Primary Key, Auto Increment
- sender_id: INT, Foreign Key → patients(id) ou doctors(id)
- receiver_id: INT, Foreign Key → patients(id) ou doctors(id)
- message_text: TEXT
- sent_at: DATETIME, NOT NULL
- read_at: DATETIME

---

## MongoDB Collection Design

### Collection: prescriptions
```json
{
  "_id": "ObjectId('64abc123456')",
  "patient_id": 101,
  "appointment_id": 51,
  "medication": "Paracétamol",
  "dosage": "500mg",
  "doctor_notes": "Prenez 1 comprimé toutes les 6 heures.",
  "refill_count": 2,
  "pharmacy": {
    "name": "Walgreens SF",
    "location": "Market Street"
  },
  "tags": ["pain", "fever", "follow-up"],
  "metadata": {
    "created_at": "2025-08-25T10:00:00Z",
    "updated_at": "2025-08-25T12:00:00Z"
  }
}

```
### Collection: feedback

```json
{
  "_id": "ObjectId('64def456789')",
  "user_id": 101,
  "appointment_id": 51,
  "comments": "Le médicament a été efficace, quelques effets secondaires légers.",
  "rating": 4,
  "tags": ["efficacité", "effets secondaires"],
  "metadata": {
    "submitted_at": "2025-08-25T12:34:56Z",
    "updated_at": "2025-08-25T14:00:00Z"
  },
  "responses": [
    {
      "response_id": "resp001",
      "admin_id": 1,
      "reply": "Merci pour votre retour, nous sommes contents que cela ait fonctionné.",
      "timestamp": "2025-08-25T15:00:00Z"
    }
  ]
}

```

### Collection: messages

```json
{
  "_id": "ObjectId('64ghi987654')",
  "sender_id": 101,
  "receiver_id": 201,
  "message_text": "Bonjour Dr. Smith, je souhaite reporter mon rendez-vous.",
  "sent_at": "2025-08-25T09:00:00Z",
  "read_at": null,
  "metadata": {
    "device": "Chrome Browser",
    "ip_address": "192.168.1.5"
  }
}

```

### Collection: logs

```json
{
  "_id": "ObjectId('64jkl543210')",
  "user_id": 101,
  "action": "Login",
  "timestamp": "2025-08-25T09:00:00Z",
  "metadata": {
    "ip_address": "192.168.1.5",
    "device": "Chrome Browser"
  }
}

```

# Notes et justification des choix

## MySQL
- Utilisé pour les données relationnelles structurées : patients, médecins, rendez-vous, administrateurs, paiements, messages.
- Les relations clés étrangères assurent l’intégrité référentielle.

## MongoDB
- Utilisé pour les données flexibles ou évolutives : prescriptions, feedback, messages détaillés, logs.
- Les prescriptions et feedback contiennent des documents imbriqués et des métadonnées, nécessitant un stockage flexible.
- Permet des documents imbriqués pour les réponses aux feedbacks ou logs, simplifiant la récupération et l’évolution du schéma.

