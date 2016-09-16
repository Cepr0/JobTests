# Query to rank order by votes
# (rank = 10)
select name, votes, round(votes / 10) as rank
from test.votes
group by name, votes, round(votes / 10)
order by round(votes / 10) desc;
