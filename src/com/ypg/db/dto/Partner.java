/*
 * This file was generated - do not edit it directly !!
 * Generated by AuDAO tool, a product of Spolecne s.r.o.
 * For more information please visit http://www.spoledge.com
 */
package com.ypg.db.dto;

import java.sql.Date;

import com.spoledge.audao.db.dto.AbstractDto;

/**
 * This is a DTO class.
 *
 * @author generated
 */
public class Partner extends AbstractDto {

    ////////////////////////////////////////////////////////////////////////////
    // Static
    ////////////////////////////////////////////////////////////////////////////

    public static final String TABLE = "partner";

    ////////////////////////////////////////////////////////////////////////////
    // Attributes
    ////////////////////////////////////////////////////////////////////////////

    private Long id;
    private String addedBy;
    private Date date;
    private String firstName;
    private String lastName;
    private String userId;
    private String position;
    private String ipAddress;
    private boolean disabled;
    private String nationalId;
    private String nationality;
    private String homeAddress;
    private String state;
    private String postalCode;
    private String country;
    private String phoneNumber;
    private String homeNumber;
    private String email;
    private String password;
    private Long business;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new empty DTO.
     */
    public Partner() {
    }

    ////////////////////////////////////////////////////////////////////////////
    // Public
    ////////////////////////////////////////////////////////////////////////////

    public Long getId() {
        return id;
    }

    public void setId( Long _val) {
        this.id = _val;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy( String _val) {
        this.addedBy = _val;
    }

    public Date getDate() {
        return date;
    }

    public void setDate( java.util.Date _val ) {
        setDate((Date)( _val != null ? new Date( _val.getTime()) : null ));
    }

    public void setDate( Date _val) {
        this.date = _val;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName( String _val) {
        this.firstName = _val;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName( String _val) {
        this.lastName = _val;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId( String _val) {
        this.userId = _val;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition( String _val) {
        this.position = _val;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress( String _val) {
        this.ipAddress = _val;
    }

    public boolean getDisabled() {
        return disabled;
    }

    public void setDisabled( boolean _val) {
        this.disabled = _val;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId( String _val) {
        this.nationalId = _val;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality( String _val) {
        this.nationality = _val;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress( String _val) {
        this.homeAddress = _val;
    }

    public String getState() {
        return state;
    }

    public void setState( String _val) {
        this.state = _val;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode( String _val) {
        this.postalCode = _val;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry( String _val) {
        this.country = _val;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber( String _val) {
        this.phoneNumber = _val;
    }

    public String getHomeNumber() {
        return homeNumber;
    }

    public void setHomeNumber( String _val) {
        this.homeNumber = _val;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String _val) {
        this.email = _val;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword( String _val) {
        this.password = _val;
    }

    public Long getBusiness() {
        return business;
    }

    public void setBusiness( Long _val) {
        this.business = _val;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * Uses 'columns' equality type.
     */
    @Override
    public boolean equals( Object _other ) {
        if (_other == this) return true;
        if (_other == null || (!(_other instanceof Partner))) return false;

        Partner _o = (Partner) _other;

        if ( id == null ) {
            if ( _o.id != null ) return false;
        }
        else if ( _o.id == null || id.longValue() != _o.id.longValue()) return false;

        if ( addedBy == null ) {
            if ( _o.addedBy != null ) return false;
        }
        else if ( _o.addedBy == null || !addedBy.equals( _o.addedBy )) return false;

        if ( date == null ) {
            if ( _o.date != null ) return false;
        }
        else if ( _o.date == null || date.getTime() != _o.date.getTime()) return false;

        if ( firstName == null ) {
            if ( _o.firstName != null ) return false;
        }
        else if ( _o.firstName == null || !firstName.equals( _o.firstName )) return false;

        if ( lastName == null ) {
            if ( _o.lastName != null ) return false;
        }
        else if ( _o.lastName == null || !lastName.equals( _o.lastName )) return false;

        if ( userId == null ) {
            if ( _o.userId != null ) return false;
        }
        else if ( _o.userId == null || !userId.equals( _o.userId )) return false;

        if ( position == null ) {
            if ( _o.position != null ) return false;
        }
        else if ( _o.position == null || !position.equals( _o.position )) return false;

        if ( ipAddress == null ) {
            if ( _o.ipAddress != null ) return false;
        }
        else if ( _o.ipAddress == null || !ipAddress.equals( _o.ipAddress )) return false;

//        if ( disabled == null ) {
//            if ( _o.disabled != null ) return false;
//        }
//        else if ( _o.disabled == null || disabled.booleanValue() != _o.disabled.booleanValue()) return false;

        if ( nationalId == null ) {
            if ( _o.nationalId != null ) return false;
        }
        else if ( _o.nationalId == null || !nationalId.equals( _o.nationalId )) return false;

        if ( nationality == null ) {
            if ( _o.nationality != null ) return false;
        }
        else if ( _o.nationality == null || !nationality.equals( _o.nationality )) return false;

        if ( homeAddress == null ) {
            if ( _o.homeAddress != null ) return false;
        }
        else if ( _o.homeAddress == null || !homeAddress.equals( _o.homeAddress )) return false;

        if ( state == null ) {
            if ( _o.state != null ) return false;
        }
        else if ( _o.state == null || !state.equals( _o.state )) return false;

        if ( postalCode == null ) {
            if ( _o.postalCode != null ) return false;
        }
        else if ( _o.postalCode == null || !postalCode.equals( _o.postalCode )) return false;

        if ( country == null ) {
            if ( _o.country != null ) return false;
        }
        else if ( _o.country == null || !country.equals( _o.country )) return false;

        if ( phoneNumber == null ) {
            if ( _o.phoneNumber != null ) return false;
        }
        else if ( _o.phoneNumber == null || !phoneNumber.equals( _o.phoneNumber )) return false;

        if ( homeNumber == null ) {
            if ( _o.homeNumber != null ) return false;
        }
        else if ( _o.homeNumber == null || !homeNumber.equals( _o.homeNumber )) return false;

        if ( email == null ) {
            if ( _o.email != null ) return false;
        }
        else if ( _o.email == null || !email.equals( _o.email )) return false;

        if ( password == null ) {
            if ( _o.password != null ) return false;
        }
        else if ( _o.password == null || !password.equals( _o.password )) return false;

        if ( business == null ) {
            if ( _o.business != null ) return false;
        }
        else if ( _o.business == null || business.longValue() != _o.business.longValue()) return false;

        return true;
    }

    /**
     * Returns a hash code value for the object.
     */
    @Override
    public int hashCode() {
        int _ret = 871724200; // = "Partner".hashCode()
        _ret += id == null ? 0 : (int)(id ^ (id >>> 32));
        _ret = 29 * _ret + (addedBy == null ? 0 : addedBy.hashCode());
        _ret = 29 * _ret + (date == null ? 0 : (int)date.getTime());
        _ret = 29 * _ret + (firstName == null ? 0 : firstName.hashCode());
        _ret = 29 * _ret + (lastName == null ? 0 : lastName.hashCode());
        _ret = 29 * _ret + (userId == null ? 0 : userId.hashCode());
        _ret = 29 * _ret + (position == null ? 0 : position.hashCode());
        _ret = 29 * _ret + (ipAddress == null ? 0 : ipAddress.hashCode());
//        _ret = 29 * _ret + (disabled == null ? 0 : (disabled ? 1 : 0));
        _ret = 29 * _ret + (nationalId == null ? 0 : nationalId.hashCode());
        _ret = 29 * _ret + (nationality == null ? 0 : nationality.hashCode());
        _ret = 29 * _ret + (homeAddress == null ? 0 : homeAddress.hashCode());
        _ret = 29 * _ret + (state == null ? 0 : state.hashCode());
        _ret = 29 * _ret + (postalCode == null ? 0 : postalCode.hashCode());
        _ret = 29 * _ret + (country == null ? 0 : country.hashCode());
        _ret = 29 * _ret + (phoneNumber == null ? 0 : phoneNumber.hashCode());
        _ret = 29 * _ret + (homeNumber == null ? 0 : homeNumber.hashCode());
        _ret = 29 * _ret + (email == null ? 0 : email.hashCode());
        _ret = 29 * _ret + (password == null ? 0 : password.hashCode());
        _ret = 29 * _ret + (business == null ? 0 : (int)(business ^ (business >>> 32)));

        return _ret;
    }


    ////////////////////////////////////////////////////////////////////////////
    // Protected
    ////////////////////////////////////////////////////////////////////////////
		
    /**
     * Constructs the content for the toString() method.
     */
    protected void contentToString(StringBuffer sb) {
        append( sb, "id", id );
        append( sb, "addedBy", addedBy );
        append( sb, "date", date );
        append( sb, "firstName", firstName );
        append( sb, "lastName", lastName );
        append( sb, "userId", userId );
        append( sb, "position", position );
        append( sb, "ipAddress", ipAddress );
        append( sb, "disabled", disabled );
        append( sb, "nationalId", nationalId );
        append( sb, "nationality", nationality );
        append( sb, "homeAddress", homeAddress );
        append( sb, "state", state );
        append( sb, "postalCode", postalCode );
        append( sb, "country", country );
        append( sb, "phoneNumber", phoneNumber );
        append( sb, "homeNumber", homeNumber );
        append( sb, "email", email );
        append( sb, "password", password );
        append( sb, "business", business );
    }
}
