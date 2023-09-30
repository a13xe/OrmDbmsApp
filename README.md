
<div align="center">
<img src="https://img.shields.io/github/repo-size/a13xe/OrmDbmsApp"/>
<img src="https://img.shields.io/badge/license-MIT-blue"/>
<img src="https://img.shields.io/github/last-commit/a13xe/OrmDbmsApp"/>
<img src="https://img.shields.io/github/downloads/a13xe/OrmDbmsApp/total"/>
</div>
<div align="center">
<img src="https://tokei.rs/b1/github/a13xe/OrmDbmsApp?category=files"/>
<img src="https://tokei.rs/b1/github/a13xe/OrmDbmsApp?category=lines"/> 
</div>


:desktop_computer: Orm DBMS App
------------------------------------------------------------------------------------------------------------------------------------------------


This ORM DBMS App is a Java-based desktop application built using Swing GUI, Maven, and Hibernate ORM. The application provides a user interface to interact with a database called `pc`. 
The database contains several tables, including `brand`, `chipset`, `cpu`, `gpu`, `mbrd` (motherboard) and `socket`.

The main purpose of the DBMS App is to provide users with a graphical interface to manage and retrieve data related to computer components. 
The application allows users to perform `CRUD` (`Create`, `Read`, `Update`, `Delete`) operations on the database tables. 
Also the App allows you to create a `PDF` document from a table.

<div align="center">
<img width=98% src="https://github.com/a13xe/OrmDbmsApp/blob/main/ASSETS/GUI/readme.png?raw=true"/>
</div>


:hammer_and_wrench: Installation
------------------------------------------------------------------------------------------------------------------------------------------------


#### `✅ Step 1.` Clone or download the project

- If you're using Git, you can clone the project using the following command:
  ```
  git clone https://github.com/AlexeyLepov/OrmDbmsApp
  ```
- Alternatively, you can download the project as a ZIP file from the repository and extract it to your desired location.

#### `✅ Step 2.` Set up the database

- Create a MySQL database named `pc`. 
- You can easily do it by running [db_final.sql](https://github.com/a13xe/OrmDbmsApp/blob/main/SQL/db_final.sql).

#### `✅ Step 3.` Configure the database connection

- Open the [hibernate.cfg.xml](https://github.com/a13xe/OrmDbmsApp/blob/main/src/main/resources/hibernate.cfg.xml).
- Modify the following properties with your MySQL database connection details:
```
<property name="hibernate.connection.url">jdbc:mysql://localhost:3306/pc</property>
<property name="hibernate.connection.username">your_username</property>
<property name="hibernate.connection.password">your_password</property>
```

#### `✅ Step 4.` Run the project

- Now you can simply run `DBMSApp.java` in `src/main/java/al/exe`. 
- Basically, you are good to go.
