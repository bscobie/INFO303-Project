package router;

/**
 *
 * @author benscobie
 */
public class GroupUpdater {
    
    public String updateGroup(String updatedGroup, String currentGroup) {
        String newGroup;
        
        if(updatedGroup.equals("Regular Customers")) {
            newGroup = "0afa8de1-147c-11e8-edec-2b197906d816";
        } else {
            newGroup = "0afa8de1-147c-11e8-edec-201e0f00872c";
        }
        
        if(currentGroup.equals(newGroup)) {
            return currentGroup;
        } else if(!currentGroup.equals(newGroup)) {
            return newGroup;
        }
        return null;
    }
    
}
