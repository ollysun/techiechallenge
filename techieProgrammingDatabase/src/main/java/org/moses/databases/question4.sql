SELECT
    userId AS UserId,
    AVG(duration) AS AverageDuration
FROM sessions
GROUP BY userId
HAVING COUNT(*) > 1;