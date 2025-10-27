-- query1.sql
SELECT DISTINCT(salary) FROM emp ORDER BY salary DESC LIMIT 1 OFFSET 1;

-- query2.sql
SELECT MAX(salary) FROM emp WHERE salary < (SELECT MAX(salary) FROM emp);

-- query3.sql
SELECT salary FROM (SELECT DISTINCT salary FROM emp ORDER BY salary DESC LIMIT 2) AS emp ORDER BY salary LIMIT 1;

-- query4.sql
SELECT DISTINCT salary FROM (SELECT salary FROM emp ORDER BY salary DESC LIMIT 2) AS emp ORDER BY salary LIMIT 1;