To Do
=====

* Add QR codes with CSS when printed
```
@media print {
	headerHolder::before {
		content: url(http://chart.apis.google.com/chart?chs=120x120&cht=qr&chl=http%3A%2F%2Fexample.org/);
	}
}
```

* Remove objectsvg-imgpng thing, everyone will have to deal with SVG now (better for printing).  (Check if still true.)


* for idempiere from Carlos
https://groups.google.com/forum/#!topic/idempiere/pwZWQv0ZIO4

OK, collecting here what we chat on IRC  :-)

1 - it will be good to update the list of tables per module including the new idempiere tables here
http://wiki.idempiere.org/en/Tables_by_Module
(and also dropping those that we stopped using)

2 - after that is really easy to generate this:
http://www.adempiere.com/technical/340/schemaspy/

I generated there per module information - including graphical ERD, and a complete list (the last link) without the graphics

3 - I just patched slightly the schemaspy base code - mostly to drop the donate links and format the dates

4 - the generation (in case somebody else wants to try it) is done using some sort of (example to generate just two tables):
java net.sourceforge.schemaspy.Main -i "(AD_ATTACHMENT)|(AD_CHANGELOG)" -o /tmp -desc "Sample" 
-u adempiere -s adempiere -t pgsql -port 5432 -host localhost -db idempiere -p adempiere -nologo -noimplied -dp $DRIVERPATH

Last parameter is the path to the postgresql.jar driver

I also patched pgsql.properties to get the table/column comments from dictionary adding these:
selectTableCommentsSql=select lower(tablename) as table_name, coalesce(description, name) as comments from ad_table
selectColumnCommentsSql=select t.tablename as table_name, c.columnname as column_name, coalesce(c.description, c.name) as comments from ad_table t join ad_column c on (t.ad_table_id = c.ad_table_id)

I'll upload a recent version next week (when I have better broadband connection) / and maybe we can think to automate the generation process with jenkins.