# AOP
A project to implement the retry and authorization concerns for a profile service through Aspect Oriented Programming (AOP).

The profile service allows access to and sharing of profiles. One can share his profile with others, and can share with others the profiles he is shared with too.

The profile service is defined as follows:
```java

public interface ProfileService { 
/**
* Read the profile of another user or oneself.
* @param userId the ID of the current user
* @param profileUserId the ID of user, whose profile is being requested
* @return the profile for profileUserId
*/
Profile readProfile(String userId, String profileUserId)   throws AccessDeniedExeption,
NetworkException ;
/**
* Share a profile with another user. The profile may or may not belong to the current user. * @param userId the ID of the current user
* @param profileUserId the ID of the user, whose profile is being shared
* @param targetUserId the ID of the user to share the profile with
*/
void shareProfile(String userId, String profileUserId, String targetUserId)   throws
AccessDeniedExeption, NetworkException ;
/**
* Unshare the current user's own profile with another user.
* @param userId
* @param targetUserId
*/
void unshareProfile(String userId, String targetUserId)   throws AccessDeniedExeption,
NetworkException ; }
```
### Features
Uses AOP to enforce the following authorization and retry policies
1. One can share his profile with anybody.
2. One can only read profiles that are shared with him, or his own profile. In any other case, an **AccessDeniedExeption** is thrown.
    * **Example**: If Alice shares her profile with Bob, Bob can further share Alice’s profile with Carl. If Alice attempts to share Bob’s profile with Carl while Bob’s profile is not shared with Alice in the first place, Alice gets an AccessDeniedExeption.
3. One can only unshare his own profile. 
    * **Example**: When unsharing a profile with Bob that the profile is not shared by any means with Bob in the first place, the operation throws an AccessDeniedExeption .
4. Both sharing and unsharing of Alice’s profile with Alice have no effect.
    * **Example**: Alice always has access to her own profile, and can share and unshare with herself without encountering any exception, even these operations do not take any effect.
   ###### Note: access control here assumes that authentication is already taken care of elsewhere, i.e., it’s outside the scope of the project to make sure only Alice can call readProfile with userId as “Alice”.    
5. All the methods in ProfileService can also run into network failures, in which case, an **NetworkException**  will be thrown, and the method takes no effect at all. Actually, since network failure happens relatively frequently, the feature is to automatically retry for up to two times for a network failure (indicated by an NetworkException). 
   ###### Note: the two retries are in addition to the original failed invocation; if you still encounter NetworkException on your second retry, you should throw out this NetworkException so that the caller knows its occurrence.

    
    
