# Query to rank order by votes
# (rank = 10)
SELECT name, votes, round(votes / 10) as rank
FROM test.votes
GROUP BY name, votes, round(votes / 10)
ORDER BY round(votes / 10) DESC;
