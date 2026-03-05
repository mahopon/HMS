# Hospital Management System (HMS)

## Overview
The Hospital Management System is a comprehensive Java-based application designed for the SC2002 Object-Oriented Programming module. It demonstrates principles of object-oriented programming including abstraction, encapsulation, inheritance, polymorphism, and design patterns such as repository pattern, singleton pattern, and MVC architecture.

## Project Structure

```
sc2002/Project/
├── src/
│   ├── Main.java                          # Application entry point
│   ├── boundary/                          # UI Layer (Presentation Layer)
│   │   ├── EntryUI.java                  # Login screen and user authentication
│   │   └── user/
│   │       ├── administrator/
│   │       │   └── A_HomeUI.java         # Administrator interface
│   │       ├── doctor/
│   │       │   └── D_HomeUI.java         # Doctor interface
│   │       ├── patient/
│   │       │   └── P_HomeUI.java         # Patient interface
│   │       └── pharmacist/
│   │           └── PH_HomeUI.java        # Pharmacist interface
│   ├── control/                           # Controller Layer (Business Logic)
│   │   ├── appointment/
│   │   │   └── AppointmentController.java # Appointment management
│   │   ├── billing/
│   │   │   └── InvoiceController.java    # Invoice generation
│   │   ├── medicine/
│   │   │   ├── MedicineController.java   # Medicine inventory
│   │   │   └── PrescriptionController.java # Prescription management
│   │   ├── notification/
│   │   │   └── NotificationController.java # Notifications
│   │   ├── prescription/
│   │   │   └── PrescriptionItemController.java # Prescription items
│   │   ├── request/
│   │   │   └── MedicineRequestController.java # Medicine requests
│   │   └── user/
│   │       ├── SessionManager.java       # User session management
│   │       ├── UserController.java       # User authentication
│   │       ├── PatientController.java    # Patient operations
│   │       ├── HospitalStaffController.java # Staff operations
│   │       └── UnavailableDateController.java # Doctor availability
│   ├── entity/                            # Data Layer (Domain Objects)
│   │   ├── user/
│   │   │   ├── User.java                 # Abstract base user class
│   │   │   ├── Administrator.java        # Admin class
│   │   │   ├── Doctor.java               # Doctor class
│   │   │   ├── Patient.java              # Patient class
│   │   │   ├── Pharmacist.java           # Pharmacist class
│   │   │   ├── StaffFactory.java         # Factory for creating staff
│   │   │   └── UnavailableDate.java      # Doctor unavailable slots
│   │   ├── appointment/
│   │   │   └── Appointment.java          # Appointment entity
│   │   ├── medicine/
│   │   │   ├── Medicine.java             # Medicine inventory
│   │   │   ├── Prescription.java         # Prescription entity
│   │   │   └── PrescriptionItem.java     # Prescription item entity
│   │   ├── billing/
│   │   │   └── Invoice.java              # Invoice entity
│   │   └── notification/
│   │       └── Notification.java         # Notification entity
│   ├── repository/                        # Data Access Layer
│   │   ├── Repository.java               # Base repository class
│   │   ├── appointment/
│   │   │   └── AppointmentRepository.java # Appointment persistence
│   │   ├── medicine/
│   │   │   ├── MedicineRepository.java   # Medicine persistence
│   │   │   ├── PrescriptionRepository.java # Prescription persistence
│   │   │   └── PrescriptionItemRepository.java # Prescription item persistence
│   │   ├── user/
│   │   │   ├── PatientRepository.java    # Patient persistence
│   │   │   ├── StaffRepository.java      # Staff persistence
│   │   │   └── UnavailableDateRepository.java # Availability persistence
│   │   ├── billing/
│   │   │   └── InvoiceRepository.java    # Invoice persistence
│   │   └── notification/
│   │       └── NotificationRepository.java # Notification persistence
│   ├── utility/                          # Utility Classes
│   │   ├── CSV_handler.java              # CSV file operations
│   │   ├── InputHandler.java             # Console input handling
│   │   ├── Password_hash.java            # Password hashing
│   │   ├── DateFormat.java               # Date formatting utilities
│   │   ├── DateTimeSelect.java           # DateTime picker
│   │   ├── ClearConsole.java             # Console clearing
│   │   └── KeystrokeWait.java            # Console waiting utilities
│   ├── exception/                         # Custom Exception Classes
│   │   ├── EntityNotFoundException.java  # Entity not found error
│   │   ├── InvalidInputException.java    # Invalid input error
│   │   └── user/
│   │       ├── InvalidUserTypeException.java # Invalid user type
│   │       ├── InvalidDurationException.java # Invalid duration
│   │       └── NoUserLoggedInException.java # Not logged in error
│   ├── interfaces/                        # Interface Definitions
│   │   ├── observer/
│   │   │   └── IObserver.java            # Observer pattern interface
│   │   ├── repository/
│   │   │   └── IRepository.java          # Repository pattern interface
│   │   ├── boundary/
│   │   │   └── IUserInterface.java       # UI interface
│   │   ├── controller/
│   │   │   ├── ICreate.java              # Create operation interface
│   │   │   ├── IDelete.java              # Delete operation interface
│   │   │   ├── IUpdate.java              # Update operation interface
│   │   │   └── ISearch.java              # Search operation interface
│   │   └── validation/
│   │       └── IValidator.java           # Validation interface
│   └── observer/
│       └── NotificationObserver.java     # Notification observer
├── data/                                  # Data files (CSV format)
│   ├── Appointment_List.csv
│   ├── Medicine_List.csv
│   ├── Patient_List.csv
│   ├── Staff_List.csv
│   └── ...
├── bin/                                   # Compiled output
├── docs/                                  # Documentation
└── .vscode/                               # VS Code settings
```

## Key Features

### User Roles
1. **Administrator** - System management and user administration
2. **Doctor** - Manage appointments, view patient history, prescribe medications
3. **Patient** - Book appointments, view medical history, request prescriptions
4. **Pharmacist** - Manage medicine inventory, process prescriptions

### Core Functionalities

#### Appointment Management
- Schedule new appointments
- View available time slots
- Reschedule appointments
- Complete appointments with diagnosis and prescription
- Cancel appointments
- View appointment history (past and scheduled)

#### Pharmacy Management
- Medicine inventory management (add, update, remove)
- Prescription management
- Prescription item management
- Medicine requests from doctors
- Invoice generation

#### User Management
- User registration and login
- Role-based access control
- Password management with hashing
- User session tracking

#### Notification System
- Observer pattern implementation
- Role-based notifications

## Design Patterns & Principles

### Object-Oriented Principles
- **Encapsulation**: Data hiding and controlled access through getters/setters
- **Inheritance**: Hierarchical user structure (User → Doctor/Patient/Admin/Pharmacist)
- **Polymorphism**: Dynamic method invocation through interface and abstract classes
- **Abstraction**: Abstract classes and interfaces define core functionality

### Design Patterns
1. **MVC Architecture**: Separation of concerns between UI, Controller, and Entity layers
2. **Repository Pattern**: Data access abstraction with Repository base class
3. **Singleton Pattern**: Repository instances are singletons
4. **Factory Pattern**: StaffFactory for creating user objects
5. **Observer Pattern**: NotificationObserver for event-driven notifications
6. **Strategy Pattern**: Interface segregation (ICreate, IDelete, IUpdate, ISearch)

## Data Storage

- CSV file-based persistence for all entities
- Automatic data loading and saving
- Unique ID generation for entities (e.g., APPT, MED, PAT)

## Technology Stack

- **Language**: Java
- **Persistence**: CSV files
- **UI**: Console-based (CLI)
- **Design**: OOP principles

## Running the Application

1. Navigate to `sc2002/Project/src` directory
2. Compile all Java files
3. Run `Main.java`

Example:
```bash
cd sc2002/Project/src
javac Main.java
java Main
```

## Future Enhancements

Currently in progress - GUI development planned for the application to replace console-based interface with graphical user interface components.

## Author

SC2002 Object-Oriented Programming Project