
# Safe Transact: High-Concurrency Transaction & Data Isolation Engine

Safe Transact is a high-performance, database-level resource management engine built to resolve data inconsistency issues, conflicts, and **Race Conditions** in web applications operating under high-concurrency workloads. 

The system leverages a **Pessimistic Locking** architecture across two separate domain modules to simulate sudden traffic spikes, preventing over-allocation and ensuring absolute data isolation without manual intervention.
# Safe Transact: High-Concurrency Transaction & Data Isolation Engine

Safe Transact is a high-performance, database-level resource management engine built to resolve data inconsistency issues, conflicts, and **Race Conditions** in web applications operating under high-concurrency workloads. 

The system leverages a **Pessimistic Locking** architecture across two separate domain modules to simulate sudden traffic spikes, preventing over-allocation and ensuring absolute data isolation without manual intervention.

---

## 🎯 Core Technical Problems Resolved

1. **Race Conditions:** Occurs when hundreds of concurrent worker threads attempt to read and write to a shared resource (e.g., academic quotas or stock balances) at the exact same millisecond, leading to corrupted data states or over-allocation via Dirty Reads.
2. **Double Spending:** Financial vulnerability where lack of thread serialization permits accounts to spend past their predefined balances during simultaneous payment dispatches.

---

## 🛠️ Tech Stack

* **Backend:** Java 23, Spring Boot v4.0.6, Spring Data JPA
* **Database:** PostgreSQL (Row-Level Serialization)
* **Frontend:** HTML5, CSS3 (Tailwind CSS UI), Asynchronous JavaScript (Promise.all Thread Interleaving Simulator)
* **Build Tool:** Maven

---

## 🧠 Architectural Design & Locking Strategy

To bulletproof systems during sudden transaction floods, the engine implements a highly conservative **Pessimistic Write Lock** strategy. This prioritizes strict data accuracy over naive parallel throughput.

### Database-Level Row Locking
By declaring the `@Lock(LockModeType.PESSIMISTIC_WRITE)` annotation over Spring Data JPA repositories, Hibernate injects native row-level exclusive locks into the underlying PostgreSQL engine:
```sql
SELECT id, current_used, quota_limit FROM advisors WHERE id = ? FOR NO KEY UPDATE;

```

When a thread executes this query, PostgreSQL isolates that specific record. Concurrent worker threads arriving at the same millisecond are forced into a strict, microsecond-level execution queue. They remain blocked until the initial thread finishes its operations and releases the lock via a database `COMMIT`.

---

## 📊 Live Simulation Scenarios & Benchmarks

The system features an integrated asynchronous frontend dashboard. It benchmarks the database isolation layers by blasting **20 parallel HTTP POST requests at the exact same millisecond**.

### Module 1: Academic Registration & Quota Enforcement

* **Scenario:** A university course with a strict capacity limit of **5 students** receives 20 simultaneous registrations.
* **Result:** The pessimistic lock serialized the requests. The first 5 threads successfully completed the database lifecycle (`SUCCESS`), while the remaining 15 threads were instantly blocked and gracefully rejected with a `QUOTA FULL` error message. The quota boundary remained unbroken.

### Module 2: Fintech & Stock Market Order Matching

* **Scenario:** A high-frequency stock pool containing an available supply of only 4 LOTS (200 TRY balance) is bombarded with 20 simultaneous buy orders.
* **Result:** Double spending was fully mitigated. Exactly 4 trades were sequentially cleared, bringing the pool balance precisely down to `0.00`. The remaining 16 trade attempts were instantly aborted with an `ORDER CANCELLED: Out of Stock!` response.

---

## 🚀 Local Installation & Setup

### 1. Database Configuration

Create a database named `safetransact` in your PostgreSQL instance and seed the initial records using the exact column mappings below:

```sql
CREATE TABLE advisors (
    id SERIAL PRIMARY KEY, 
    current_used INT NOT NULL DEFAULT 0, 
    quota_limit INT NOT NULL DEFAULT 5
);

CREATE TABLE wallets (
    id SERIAL PRIMARY KEY, 
    balance DECIMAL(10,2) NOT NULL, 
    currency VARCHAR(255) NOT NULL
);

-- Seed initial test states
INSERT INTO advisors (id, current_used, quota_limit) VALUES (1, 0, 5);
INSERT INTO wallets (id, balance, currency) VALUES (1, 200, 'TRY');

```

### 2. Launching the Backend Application

Navigate to the root directory of the project and execute the application server using the Maven wrapper:

```bash
mvn spring-boot:run

```

### 3. Accessing the Dashboard UI

Open your web browser and navigate to the local loopback address to access the asynchronous concurrency panel:

```text
http://localhost:8080/

```

```

```
