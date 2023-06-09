![](https://img.shields.io/tokei/lines/github.com/AlexeyLepov/OrmDbmsApp?style=for-the-badge)
![CodeSize](https://img.shields.io/github/languages/code-size/AlexeyLepov/OrmDbmsApp?style=for-the-badge)
![Repo](https://img.shields.io/github/repo-size/AlexeyLepov/OrmDbmsApp?style=for-the-badge)
![LastCommint](https://img.shields.io/github/last-commit/AlexeyLepov/OrmDbmsApp?style=for-the-badge)

Orm DBMS app (mysql & maven)
=======================================================================

This ORM DBMS App is a Java-based desktop application built using Swing GUI, Maven, and Hibernate ORM. The application provides a user interface to interact with a database called `pc`. 
The database contains several tables, including `brand`, `chipset`, `cpu`, `gpu`, `mbrd` (motherboard) and `socket`.

The main purpose of the DBMS App is to provide users with a graphical interface to manage and retrieve data related to computer components. 
The application allows users to perform `CRUD` (`Create`, `Read`, `Update`, `Delete`) operations on the database tables. 
Also the App allows you to create a `PDF` document from a table.

![readme](https://github.com/AlexeyLepov/OrmDbmsApp/assets/77492646/86c7b2f9-cfca-41e5-91c3-33ffb69eef5e)

The DBMS App features a user-friendly interface built using Swing GUI, providing a seamless and intuitive experience for users.
It leverages the power of Hibernate ORM to interact with the database, enabling efficient data retrieval, manipulation, and persistence.


Installation
======================================================================================


$${\color{lightgreen} Step \space 1. \space Clone \space or \space download \space the \space project }$$
--------------------------------------------------------------------------------------
- If you're using Git, you can clone the project using the following command:
  ```
  git clone https://github.com/AlexeyLepov/OrmDbmsApp
  ```
- Alternatively, you can download the project as a ZIP file from the repository and extract it to your desired location.


$${\color{lightgreen} Step \space 2. \space Set \space up \space the \space database }$$
--------------------------------------------------------------------------------------
- Create a MySQL database named `pc`. 
- You can easily do it by running `db_final.sql` file (https://github.com/AlexeyLepov/OrmDbmsApp/blob/main/SQL/db_final.sql)


$${\color{lightgreen} Step \space 3. \space Configure \space the \space database \space connection }$$
--------------------------------------------------------------------------------------
- Open the `hibernate.cfg.xml` file located in the `src/main/resources` directory.
- Modify the following properties with your MySQL database connection details:
  ```
  <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/pc</property>
  <property name="hibernate.connection.username">your_username</property>
  <property name="hibernate.connection.password">your_password</property>
  ```


$${\color{lightgreen} Step \space 4. \space Run \space the \space project }$$
--------------------------------------------------------------------------------------
- Now you can simply run `DBMSApp.java` in `src/main/java/al/exe`. 
- Basically, you are good to go.


$${\color{lightgreen} (Optional) \space Step \space 5. \space Build \space the \space project }$$
--------------------------------------------------------------------------------------
- Open a command prompt or terminal and navigate to the project directory.
- Run the following Maven command to build the project:
  ```
  mvn clean package
  ```


$${\color{lightgreen} (Optional) \space Step \space 6. \space Run \space the \space application }$$
--------------------------------------------------------------------------------------
- After the build process is complete, you will find a JAR file named dbms-app.jar in the target directory.
- Run the application using the following command:
  ```
  java -jar target/dbms-app.jar
  ```


$${\color{lightgreen} (Optional) \space Step \space 7. \space Use \space the \space application }$$
--------------------------------------------------------------------------------------
- The application window will open, allowing you to interact with the GUI.
- You can add, view, update, and delete records in the database tables using the provided functionality.

