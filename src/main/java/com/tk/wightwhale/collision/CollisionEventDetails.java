package com.tk.wightwhale.collision;

import javax.xml.bind.annotation.*;

/**
 * CollisionEventDetails is a decorator which
 * extends CollisionEvents to include the cause
 * of the collision (e.g. the groups of the two
 * involved)
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso ({CollisionEvent.class})
public class CollisionEventDetails extends CollisionEvent{
    @XmlTransient
    private static final String TAG = "CollisionEventDetails";

    @XmlElement (name = "Collider")
    private String movingGroup;
    @XmlElement (name = "Collidee")
    private String otherGroup;

    /**
     * Default constructor; sets movingGroup to player and otherGroup to scenery
     */
    public CollisionEventDetails(){
        super();
        movingGroup = "player"; otherGroup = "scenery";
    }

    /**
     * Custom constructor
     * @param type      CollisionType enum type of event
     * @param actionCode    action code integer
     * @param statEffect    stat effect integer
     * @param mvGroup       the group of the moving gameObject
     * @param othGroup      the group of the other gameObject
     */
    public CollisionEventDetails(CollisionType type, int actionCode, int statEffect, String mvGroup, String othGroup){
        super(type, actionCode, statEffect);
        this.movingGroup = mvGroup;
        this.otherGroup = othGroup;
    }

    /**
     * Gets the group key of the moving gameObject
     * @return String
     */
    public String getMovingGroup() {
        return movingGroup;
    }

    /**
     * Sets the group key of the moving gameObject
     * @param movingGroup string
     */
    public void setMovingGroup(String movingGroup) {
        this.movingGroup = movingGroup;
    }

    /**
     * Gets the group key of the other gameObject
     * @return String
     */
    public String getOtherGroup() {
        return otherGroup;
    }

    /**
     * Sets the group key of the other gameObject
     * @param otherGroup String group name
     */
    public void setOtherGroup(String otherGroup) {
        this.otherGroup = otherGroup;
    }

    /**
     * Creates a copy of this CollisionEventDetails object
     * which is a CollisionEvent object
     * @return CollisionEvent object with same type, actionCode and statEffect as this
     */
    public CollisionEvent toCollisionEvent(){
        return new CollisionEvent(type, actionCode, statEffect);
    }
}
