 ------------------
 DESIGN DECISIONS:
 ------------------
 
 1- User Profile singleton.
 	We can now be sure that when referring to a UserProfile, it's always the same one.
 	This make sense since since the user profile class represents the current user of the application.
 	We used the singleton pattern for its centralized management of internal resources and the added benefit of a global point of access.
 
 2- User Profile observable by JComponents (views).
 	The components of our GUI observed the User Profile for changes as it was the central point of change. For our application, if a change occured while the user is using the client, the change must have affected the state of the user profile; we want to represent these changes using our views.
 	We used the push model of the observer design pattern since it's the unique UP that informs the views of changes.

 	We extended the AbstractTableModel in order to access methods we implemented when we were only provided TableModel instances.
 
 3- Read Speeches stored as an ArrayList<Integer> which contained the hash values of the speeches.
 	The reason we designed it in this way instead of simply adding a boolean to each speech was because it is much more efficient this way. If we were to add a "read" boolean to each speech, to reset all read speeches we would need to traverse through all speeches. In our implementation, we just empty the list.

 	We generated the hashcode using only the mpKey and time instead of the content since a speech should be unique for both its English and French identities. 
 
 
 
 
 
 
 
 
 **MILESTONE 2**
The Capone class has a unique Parliament. In order to support that, we created the Parliament directly in the Capone class, 
and provide a getParliament() method that returns this unique Parliament.
That means that we had to make the constructor of the Parliament at least "protected".
Now any class of the capone.model package can create a Parliament, but we didn't write such code in any model.
Hence, we are sure that only one parliament exists.

Even though Memberships are immutable, we decided that they are equal only if they have the same Party, Riding, and StartDate.
This allows us to update the EndDate of a Membership by deleting the old one, and creating a new Membership with the same 
Party, the same Riding and the same StartDate but a different EndDate.
MPs are equal only if they have the same primary key. We can update any other field of an MP object.
In our Parliament class, we make sure that each Party is unique by storing them in a HashMap so if a Party already exists, it will not be created twice.
A Party should be immutable, so we made two Parties equals only if all their fields are the same. 
Similarly, since Ridings should also be immutable, we decided that they are equals only if they have the exact same fields.
However we assumed that each Riding has a different ID number since this ID is the key of our HashMap storing the Ridings.
With the current design, if 2 Ridings have the same ID, one will override the over even though they have different Name and Province.
Finally we decided that UserProfiles should be equals only if they have the same name.
Thus 2 different users can be interested in the exact same MPs and expressions. 

For the Observer Design Pattern, we used the Push model since only the modified MP is passed through the update method,
and not the whole model (ie: the Parliament). We then go through each field of the MP and modify what needs to be updated
(this can be thought as a pull model inside a push model: we pass the MP(push), but we don't pass what changes in the MP(pull)).

In order for a Speech to have access to his MP, we added a String field that represents the key of the MP.
We decided not to have a reference to the MP object itself because circular relations can lead to complex code and can sometimes be hard to understand.
Moreover, Gson serialization don't support such "relation loop" (ie: MP->Speech->MP->Speech->...).
**MILESTONE 2**