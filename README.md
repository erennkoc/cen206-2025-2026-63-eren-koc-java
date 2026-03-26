# 📋 Project Management Application

> **CEN206 — Object Oriented Programming Term Project | Spring 2025–2026**  
> Recep Tayyip Erdoğan University — Faculty of Engineering and Architecture — Computer Engineering Department

[![Release](https://github.com/erennkoc/cen206-2025-2026-63-eren-koc-java/actions/workflows/release.yml/badge.svg)](https://github.com/erennkoc/cen206-2025-2026-63-eren-koc-java/actions/workflows/release.yml)

## 👥 Team Members

| Name | Role | Email |
| :--- | :--- | :--- |
| **Eren Koç** | Developer & Designer | eren_koc23@erdogan.edu.tr |
| **Eren Önder** | Developer & Designer | eren_onder24@erdogan.edu.tr |
| **Ramazan Giyici** | Developer & Designer | ramazan_giyici22@erdogan.edu.tr |

---

## 📖 Project Description

A comprehensive **console-based Project Management System** built in Java, demonstrating core Object-Oriented Programming principles. The application allows users to manage projects, tasks, and team members through an interactive terminal interface.

### Key Features
- 🔐 **User Authentication** — Register, login, and guest mode access
- 📁 **Project Management** — Create, update, delete, and list projects
- ✅ **Task Tracking** — Manage tasks with statuses (TODO, IN_PROGRESS, DONE)
- 📊 **Reporting** — Generate system-wide and task-specific status reports
- 💾 **Multi-Storage Support** — Switch between Binary File, SQLite, and MySQL at runtime
- 🏭 **Factory Design Pattern** — Pluggable storage backends via `IRepository<T>` + `RepositoryFactory`

### Constraint Addressed
**Time Tracking & Managerial Overload** — The application provides automated task status tracking and report generation to help managers efficiently monitor project progress and remaining workload.

---

## 🏗️ Architecture

The project follows **N-Tier (Multi-Layer) Architecture** with strict separation of concerns:

```
┌─────────────────────────────────────────────────┐
│               Application Layer                 │
│              (Main.java — Console UI)           │
├─────────────────────────────────────────────────┤
│               Service Layer                     │
│  (UserService, ProjectService, TaskService,     │
│   ReportService)                                │
├─────────────────────────────────────────────────┤
│               Storage Layer                     │
│  IRepository<T> Interface                       │
│  ├── BinaryUserRepository / Project / Task      │
│  ├── SQLiteUserRepository / Project / Task      │
│  └── MySQLUserRepository  / Project / Task      │
│  StorageConfig + RepositoryFactory              │
└─────────────────────────────────────────────────┘
```

### OOP Principles Applied
- **Encapsulation** — Private fields with public getters/setters
- **Inheritance** — `BaseEntity` abstract class as parent for all domain models
- **Abstraction** — `IRepository<T>` generic interface for storage operations
- **Polymorphism** — `displayDetails()` overridden in each entity; runtime storage resolution

### Design Patterns
- **Factory Pattern** — `RepositoryFactory` creates the correct repository based on active `StorageType`
- **Strategy Pattern** — Storage backends are interchangeable at runtime via `StorageConfig`

---

## 📂 Project Structure

```
cen206-2025-2026-63-eren-koc-java/
├── projectmanager-app/
│   └── src/
│       ├── main/java/com/projectmanagement/
│       │   ├── app/
│       │   │   └── Main.java                  # Entry point & Console UI
│       │   └── lib/
│       │       ├── models/
│       │       │   ├── BaseEntity.java        # Abstract base class
│       │       │   ├── User.java              # User domain model
│       │       │   ├── Project.java           # Project domain model
│       │       │   ├── Task.java              # Task domain model
│       │       │   └── TaskStatus.java        # Enum (TODO, IN_PROGRESS, DONE)
│       │       ├── services/
│       │       │   ├── UserService.java       # User business logic
│       │       │   ├── ProjectService.java    # Project business logic
│       │       │   ├── TaskService.java       # Task business logic
│       │       │   └── ReportService.java     # Reporting engine
│       │       └── storage/
│       │           ├── IRepository.java       # Generic CRUD interface
│       │           ├── RepositoryFactory.java  # Factory for storage
│       │           ├── StorageConfig.java      # Active storage config
│       │           ├── StorageType.java        # Enum (BINARY, SQLITE, MYSQL)
│       │           ├── Binary*Repository.java  # Binary file implementations
│       │           ├── SQLite*Repository.java  # SQLite implementations
│       │           └── MySQL*Repository.java   # MySQL implementations
│       └── test/java/com/projectmanagement/
│           └── app/
│               └── MainTest.java              # JUnit5 test suite
├── design/
│   ├── plantuml/       # UML diagrams (.puml + .png)
│   ├── c4/             # C4 Model diagrams (.puml + .png)
│   ├── drawio/         # Deployment diagram (.drawio + .png)
│   ├── figma/console/  # Console wireframe exports (.png)
│   ├── umple/          # UMPLE domain model (.ump)
│   └── projectlibre/   # Gantt chart (.xml)
├── report/             # Project report (.docx)
├── test-coverage/      # JaCoCo HTML coverage report
├── docker-compose.yml  # MySQL Docker service
├── pom.xml             # Maven build configuration
├── LICENSE             # Open-source license
└── README.md           # This file
```

---

## 🚀 Getting Started

### Prerequisites
- **Java JDK** 11 or 17
- **Apache Maven** 3.6+
- **Docker** (optional, for MySQL backend)

### Build & Run

```bash
# Clone the repository
git clone https://github.com/erennkoc/cen206-2025-2026-63-eren-koc-java.git

# Navigate to the project
cd cen206-2025-2026-63-eren-koc-java/projectmanager-app

# Build the project
mvn clean package

# Run the application
java -jar target/projectmanager-app-1.0-SNAPSHOT.jar
```

Or use the provided batch scripts:
```bash
# Build everything (compile, test, coverage, docs)
7-build-app.bat

# Run the application
8-run-app.bat
```

### Run Tests & Generate Coverage Report

```bash
cd projectmanager-app
mvn clean test jacoco:report
```

The HTML coverage report will be generated at:  
`projectmanager-app/target/site/jacoco/index.html`

### MySQL Backend (Docker)

```bash
# Start MySQL container
docker-compose up -d

# The application will connect automatically when MySQL storage is selected
```

---

## 🗄️ Storage Backends

The application supports **three interchangeable storage backends**, selectable at runtime from the Settings menu:

| Storage Type | Technology | Data Location |
| :--- | :--- | :--- |
| **Binary File** | Java `ObjectOutputStream` / `ObjectInputStream` | `users.dat`, `projects.dat` |
| **SQLite** | JDBC (`org.xerial:sqlite-jdbc`) | `projectmanager.db` |
| **MySQL** | JDBC (`mysql:mysql-connector-java`) + Docker Compose | Remote MySQL container |

---

## 📊 Test Coverage

- **Framework:** JUnit 5 + Mockito
- **Coverage Tool:** JaCoCo
- **Target:** 100% coverage on library module

![All](assets/badge_combined.svg)
![Branch Coverage](assets/badge_branchcoverage.svg)
![Line Coverage](assets/badge_linecoverage.svg)
![Method Coverage](assets/badge_methodcoverage.svg)

---

## 📐 Design Documents

| Document | Tool | Location |
| :--- | :--- | :--- |
| Class Diagram | PlantUML | `design/plantuml/class.puml` |
| Sequence Diagrams (≥3) | PlantUML | `design/plantuml/seq-*.puml` |
| Use-Case Diagram | PlantUML | `design/plantuml/usecase.puml` |
| ER Diagram | PlantUML | `design/plantuml/er.puml` |
| Gantt Chart | PlantUML | `design/plantuml/gantt.puml` |
| C4 Model (Context/Container/Component) | PlantUML C4 | `design/c4/*.puml` |
| Deployment Diagram | draw.io | `design/drawio/deployment.drawio` |
| Domain Model | UMPLE | `design/umple/model.ump` |
| Console Wireframes | Figma | `design/figma/console/*.png` |
| Project Plan | ProjectLibre | `design/projectlibre/project.xml` |

---

## 🔧 Dependencies

| Library | Version | Purpose | License |
| :--- | :--- | :--- | :--- |
| JUnit Jupiter | 5.9.3 | Unit Testing | EPL 2.0 |
| Mockito | 4.11.0 | Test Mocking | MIT |
| SQLite JDBC | 3.41.2.1 | SQLite Driver | Apache 2.0 |
| MySQL Connector/J | 8.0.33 | MySQL Driver | GPL v2 |
| JaCoCo | 0.8.12 | Code Coverage | EPL 2.0 |
| SLF4J + Logback | 1.7.32 / 1.2.6 | Logging | MIT / EPL 1.0 |
| JLine | 3.23.0 | Console Input | BSD-3 |
| JNA | 5.13.0 | Native Access | Apache 2.0 / LGPL 2.1 |

---

## 📄 License

This project is licensed under the terms specified in the [LICENSE](LICENSE) file.

---

## 🎓 Course Information

- **Course:** CEN206 — Object Oriented Programming
- **Instructor:** Asst. Prof. Dr. Uğur CORUH
- **University:** Recep Tayyip Erdoğan University
- **Semester:** Spring 2025–2026
