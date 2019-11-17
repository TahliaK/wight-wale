package com.tk.wightwhale.collision;

import javax.xml.bind.annotation.*;

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

    public CollisionEventDetails(){
        super();
        movingGroup = "player"; otherGroup = "scenery";
    }

    public CollisionEventDetails(CollisionType type, int actionCode, int statEffect, String mvGroup, String othGroup){
        super(type, actionCode, statEffect);
        this.movingGroup = mvGroup;
        this.otherGroup = othGroup;
    }

    public String getMovingGroup() {
        return movingGroup;
    }

    public void setMovingGroup(String movingGroup) {
        this.movingGroup = movingGroup;
    }

    public String getOtherGroup() {
        return otherGroup;
    }

    public void setOtherGroup(String otherGroup) {
        this.otherGroup = otherGroup;
    }

    public CollisionEvent toCollisionEvent(){
        return new CollisionEvent(type, actionCode, statEffect);
    }
}
