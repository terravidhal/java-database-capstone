# User Stories – Smart Clinic

## Administrateur

### Story 1
**Title:**  
_As an administrator, I want to log in with my username and password so that I can securely access the platform._  

**Acceptance Criteria:**  
1. I can enter my username and password.  
2. If credentials are correct, I am redirected to the Admin Dashboard.  
3. If credentials are wrong, I see an error message.  

**Priority:** High  
**Story Points:** 3  
**Notes:** Authentication via Spring Security.  

### Story 2
**Title:**  
_As an administrator, I want to log out so that the system remains secure._  

**Acceptance Criteria:**  
1. Logout button is visible on every page.  
2. Clicking logout ends the session.  
3. User is redirected to the login page.  

**Priority:** High  
**Story Points:** 2  
**Notes:** Session should expire automatically after inactivity.  

### Story 3
**Title:**  
_As an administrator, I want to add doctors to the portal so that they can manage appointments._  

**Acceptance Criteria:**  
1. Admin can input doctor’s name, email, and specialization.  
2. System validates the email format.  
3. Doctor account is created and stored in MySQL.  

**Priority:** High  
**Story Points:** 3  
**Notes:** Send notification email to doctor after creation.  

### Story 4
**Title:**  
_As an administrator, I want to remove a doctor’s profile so that only active doctors remain on the portal._  

**Acceptance Criteria:**  
1. Admin can select a doctor from the list.  
2. Clicking remove deletes the doctor’s account from MySQL.  
3. Confirmation popup appears before deletion.  

**Priority:** Medium  
**Story Points:** 3  
**Notes:** Ensure all future appointments of the deleted doctor are reassigned.  

### Story 5
**Title:**  
_As an administrator, I want to run a stored procedure to get the number of appointments per month so that I can monitor usage._  

**Acceptance Criteria:**  
1. Admin can execute the procedure from MySQL CLI or dashboard.  
2. Result shows appointment count per month.  
3. Data can be exported as CSV.  

**Priority:** Medium  
**Story Points:** 4  
**Notes:** Use proper error handling for empty months.  

---

## Patient

### Story 1
**Title:**  
_As a patient, I want to see a list of doctors without logging in so that I can explore my options._  

**Acceptance Criteria:**  
1. Doctor list visible on the homepage.  
2. Shows doctor name, specialization, and rating.  
3. Clicking a doctor shows more details.  

**Priority:** Medium  
**Story Points:** 2  
**Notes:** Accessible without authentication.  

### Story 2
**Title:**  
_As a patient, I want to sign up using my email and password so that I can book appointments._  

**Acceptance Criteria:**  
1. Signup form collects email, password, and name.  
2. Password must meet complexity requirements.  
3. Account is created in MySQL.  

**Priority:** High  
**Story Points:** 3  
**Notes:** Send confirmation email.  

### Story 3
**Title:**  
_As a patient, I want to log in to manage my bookings._  

**Acceptance Criteria:**  
1. Login form validates credentials.  
2. Redirect to Patient Dashboard upon successful login.  
3. Error message displayed for invalid credentials.  

**Priority:** High  
**Story Points:** 2  
**Notes:** Integrate with Spring Security.  

### Story 4
**Title:**  
_As a patient, I want to log out so that my account remains secure._  

**Acceptance Criteria:**  
1. Logout button visible on dashboard.  
2. Ends session and redirects to login page.  

**Priority:** High  
**Story Points:** 1  
**Notes:** Session expires automatically after inactivity.  

### Story 5
**Title:**  
_As a patient, I want to book a one-hour appointment with a doctor so that I can see a doctor at a convenient time._  

**Acceptance Criteria:**  
1. Patient can select date, time, and doctor.  
2. System checks doctor availability.  
3. Appointment is saved in MySQL.  

**Priority:** High  
**Story Points:** 3  
**Notes:** Prevent double-booking.  

### Story 6
**Title:**  
_As a patient, I want to see my upcoming appointments so that I can prepare accordingly._  

**Acceptance Criteria:**  
1. Dashboard lists future appointments with date/time and doctor.  
2. Appointment details include location and notes.  

**Priority:** Medium  
**Story Points:** 2  
**Notes:** Data retrieved from MySQL.  

---

## Médecin

### Story 1
**Title:**  
_As a doctor, I want to log in to manage my appointments._  

**Acceptance Criteria:**  
1. Login form validates credentials.  
2. Redirect to Doctor Dashboard upon successful login.  

**Priority:** High  
**Story Points:** 2  
**Notes:** Integrate with Spring Security.  

### Story 2
**Title:**  
_As a doctor, I want to log out to protect my data._  

**Acceptance Criteria:**  
1. Logout ends session.  
2. Redirect to login page.  

**Priority:** High  
**Story Points:** 1  
**Notes:** Automatic session timeout after inactivity.  

### Story 3
**Title:**  
_As a doctor, I want to view my appointment calendar so that I can stay organized._  

**Acceptance Criteria:**  
1. Calendar displays all upcoming appointments.  
2. Appointments show patient names and times.  

**Priority:** Medium  
**Story Points:** 3  
**Notes:** Fetch from MySQL.  

### Story 4
**Title:**  
_As a doctor, I want to mark myself unavailable for certain slots so that patients only see available times._  

**Acceptance Criteria:**  
1. Doctor can select unavailable time slots.  
2. Blocked slots are not bookable by patients.  

**Priority:** Medium  
**Story Points:** 2  
**Notes:** Reflect in appointment booking API.  

### Story 5
**Title:**  
_As a doctor, I want to update my profile with specialization and contact information so that patients have up-to-date information._  

**Acceptance Criteria:**  
1. Profile form editable in dashboard.  
2. Changes saved in MySQL.  

**Priority:** Medium  
**Story Points:** 2  
**Notes:** Validate email and phone number formats.  

### Story 6
**Title:**  
_As a doctor, I want to see patient details for upcoming appointments so that I can prepare._  

**Acceptance Criteria:**  
1. Dashboard shows patient name, age, and reason for visit.  
2. Only upcoming appointments are visible.  

**Priority:** Medium  
**Story Points:** 3  
**Notes:** Data fetched securely from MySQL.  
