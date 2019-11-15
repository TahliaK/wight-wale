package com.tk.wightwhale.actors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Base class for all displayable objects
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({GameObject.class, MovingObject.class})
public abstract class AbstractGameObject {
    /** Object ID string **/
    @XmlElement
    protected String id;    //global access ID
    /** visible status **/
    @XmlElement
    protected boolean visible;

    /**
     * Returns ID value
     * @return ID of this object
     */
    public String getId() {
        return id;
    }

    /**
     * Sets ID value
     * @param id the id value to use
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns visibility state
     * @return true if visible
     */
    public boolean isVisible() { return visible; }

    /**
     * Sets visibility
     * @param state true for visible, false for invisible
     */
    public void setVisibility(boolean state) { this.visible = state; }
}
