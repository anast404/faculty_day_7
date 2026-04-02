# Задача 74
select id, if (has_internet=1, "YES","NO") as has_internet from Rooms
# Задача 56
delete from Trip where town_from="Moscow"
# Задача 114
select Pilots.name from Pilots
inner join Flights on Flights.second_pilot_id=Pilots.pilot_id
where year(Flights.flight_date)=2023
and month(Flights.flight_date)=8
and Flights.destination="New York"
# Задача 19
select DISTINCT FamilyMembers.status from FamilyMembers
inner join Payments on Payments.family_member = FamilyMembers.member_id
inner join Goods on Goods.good_id = Payments.good
where Goods.good_name="potato"
# Задача 21
SELECT Goods.good_name from Goods
inner join Payments on Payments.good = Goods.good_id
group by Payments.good having count(*)>1
# Задача 32
SELECT floor(avg(TIMESTAMPDIFF(year, birthday, CURDATE()))) as age from FamilyMembers
