<?xml version="1.0"?>
<si>
<data type="sql" >
<query>SELECT * FROM HJK_SYS_USER {where} {order}</query>
<count>SELECT COUNT(1) FROM HJK_SYS_USER {where} </count>
</data>
<view lockgrid="true">
<columns><column name="用户名" value="username" /><column name="姓名" value="fullname" /></columns>
<conditions><condition name="username" label="姓名" type="text" /></conditions>
</view>
</si>