docker run --name mysql81 -p 3406:3306 ^
-e MYSQL_ROOT_PASSWORD=1234 ^
-e MYSQL_USER=tasktracker ^
-e MYSQL_PASSWORD=tasktracker ^
-e MYSQL_DATABASE=tasktracker ^
-d mysql/mysql-server:5.7 