# Hi, it's a crud api with java Servlets and Hibernate. 

I used only standard libraries because the goal was to understand how to work with http requests without frameworks. For Spring, I have a separate project, it is also in my repository.

# Folders

controller - it's a folder with my servlets, they are responsible for handling incoming requests.<br>
database - here all classes about db. I used postgresSQL. <br>
domain - My entities. No logic, only static objects<br>
dto - for data trnsport objects<br>
exception - some custom exceptions, i just tried to implement something look like best practice.<br>
mapper - mappers to map entites to dto<br>
model - it's folder for model classes, there is only custom exception's code. <br>
service - all logic should be here, but because it's a simple demonstration app there is only crud operations.<br>
util - it's a folder with configurations and some general classes. <br>

# Description
There are only 3 entities: User, File and Event.

Each user can have multiple files. The files are stored in postgreSQL. When interacting with files, Events are created, these events have links to the user who created them and to the file on which the action was performed. Here it would be possible to use audit functions from hibernate and event listeners, but they already exist in other projects and here, after all, the goal was to work with the Jakarta library.




