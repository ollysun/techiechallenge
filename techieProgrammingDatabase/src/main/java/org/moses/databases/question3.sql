/*
LEFT JOIN (or LEFT OUTER JOIN):
- Returns all records from the left table (table1)
- Returns matching records from the right table (table2)
- If no match found in right table, NULL values are returned for right table columns

RIGHT JOIN (or RIGHT OUTER JOIN):
- Returns all records from the right table (table2)
- Returns matching records from the left table (table1)
- If no match found in left table, NULL values are returned for left table columns
*/

-- LEFT JOIN example
SELECT *
FROM employees e
LEFT JOIN departments d ON e.department_id = d.id;

-- RIGHT JOIN example
SELECT *
FROM employees e
RIGHT JOIN departments d ON e.department_id = d.id;