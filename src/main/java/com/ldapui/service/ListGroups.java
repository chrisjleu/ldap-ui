package com.ldapui.service;
/*******************************************************************************
 * $Novell: ListGroups.java,v 1.6 2003/08/21 11:34:11 $
 *
 * Copyright (C) 1999, 2000, 2001 Novell, Inc. All Rights Reserved.
 *
 * THIS WORK IS SUBJECT TO U.S. AND INTERNATIONAL COPYRIGHT LAWS AND
 * TREATIES. USE AND REDISTRIBUTION OF THIS WORK IS SUBJECT TO THE LICENSE
 * AGREEMENT ACCOMPANYING THE SOFTWARE DEVELOPMENT KIT (SDK) THAT CONTAINS
 * THIS WORK. PURSUANT TO THE SDK LICENSE AGREEMENT, NOVELL HEREBY GRANTS TO
 * DEVELOPER A ROYALTY-FREE, NON-EXCLUSIVE LICENSE TO INCLUDE NOVELL'S SAMPLE
 * CODE IN ITS PRODUCT. NOVELL GRANTS DEVELOPER WORLDWIDE DISTRIBUTION RIGHTS
 * TO MARKET, DISTRIBUTE, OR SELL NOVELL'S SAMPLE CODE AS A COMPONENT OF
 * DEVELOPER'S PRODUCTS. NOVELL SHALL HAVE NO OBLIGATIONS TO DEVELOPER OR
 * DEVELOPER'S CUSTOMERS WITH RESPECT TO THIS CODE.
 *
 * $name:         ListGroups.java
 * $description:  ListGroups sample lists the members of the specified
 *                group.  If the object is a dynamic group, the search filter,
 *                identity, and excluded member list are also displayed.
 *
 *
 * $description: The ListGroups.java sample lists the members of the
 *               specified group.  If the entry is a dynamic group,
 *               the other  dynamic group attributes are also displayed.
 *
 *               See the DynamicGroup.java sample for more information on
 *               dynamic groups.
 *
 *   NOTE:  Dynamic groups are supported in eDirectory version 8.6.1 or later.
 *
 *   Group Attributes:
 *
 *        member (or it's synonym uniqueMember):  A list of the DNs of the
 *            members of the group.   For dynamic groups, DN's added to this
 *            attribute will be automatically included in the group regardless
 *            of the search filter.
 *
 *   Dynamic Group attributes:
 *
 *        memberQueryURL:  Specifies parameters for the implied search when
 *            reading members of the group.  The query is in URL form:
 *                ldap:///<base dn>??<scope>?<filter>[?[!]x-chain]
 *
 *            where:
 *                <base dn> is the starting point of the search
 *                <scope> is "one" for one-level, or "sub" for subtree search
 *                <filter> is the search filter
 *                x-chain indicates that the search will chain if necessary.
 *                    (Use with care, since it degrades performance.)
 *
 *        excludedMember:  A list of the DN's specifically excluded from the
 *            group regardless of the search filter.
 *
 *        dgIdentity:  specifies the identity to use for the implicit search.
 *            If this attribute is not present, it uses the public/private key
 *            of the group object, if present.
 *            Otherwise use the anonymous identity.
 *
 *        dgAllowDuplicates:  Boolean attribute.  "true" speeds up the
 *            search, but the members list may have duplicates.
 *
 *        dgTimeout:  Number of seconds to wait to get results from
 *            another server when chaining.
 ******************************************************************************/

import com.novell.ldap.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.io.UnsupportedEncodingException;

/**
 * 
 * @author George
 * @deprecated
 */
public class ListGroups
{
    public static void main( String[] args )
    {
        if (args.length != 4) {
           System.out.println("Usage:   java ListGroups <host name> <login dn>"
                              + " <password> <group dn>\n");
           System.out.println("Example: java ListGroups Acme.com"
                                    + " \"cn=admin,o=Acme\" secret "
                                    + " cn=salesGroup,ou=sales,o=acme\n");
           System.exit(0);
        }

        int ldapPort = LDAPConnection.DEFAULT_PORT;
        int searchScope = LDAPConnection.SCOPE_BASE;
        int ldapVersion  = LDAPConnection.LDAP_V3;
        int i;
        Enumeration objClass =  null;
        Enumeration queryURL =  null;
        Enumeration identity =  null;
        Enumeration excludedMember = null;
        Enumeration member = null;
        boolean isGroup=false, isDynamicGroup=false;
        String[] attrs  = new String[] {
                            "objectClass",
                            "memberQueryURL",
                            "dgIdentity",
                            "excludedMember",
                            "member"};

        /* Since reading members of a dynamic group could potentially involve
         * a significant directory search, we use a timeout. Setting
         * time out to 10 seconds
         */
        LDAPSearchConstraints cons = new LDAPSearchConstraints();
        cons.setTimeLimit( 10000 ) ;

        String ldapHost = args[0];
        String loginDN  = args[1];
        String password = args[2];
        String groupDN  = args[3];

        LDAPConnection lc = new LDAPConnection();

        try {
            // connect to the server
            lc.connect( ldapHost, ldapPort );
            // bind to the server
            lc.bind( ldapVersion, loginDN, password.getBytes("UTF8") );

            System.out.println("\n\tReading object :" + groupDN);
            LDAPSearchResults searchResults =
                lc.search(  groupDN,       // object to read
                            searchScope,   // scope - read single object
                            null,          // search filter
                            attrs,         // return only required attributes
                            false,         // return attrs and values
                            cons );        // time out value

            // Examine the attributes that were returned and extract the data

            LDAPEntry nextEntry = null;
            try {
                nextEntry = searchResults.next();
            }
            catch(LDAPException e) {
                System.out.println("Error: " + e.toString());
                System.exit(1);
            }

            LDAPAttributeSet attributeSet = nextEntry.getAttributeSet();
            Iterator allAttributes = attributeSet.iterator();

            while(allAttributes.hasNext()) {
                LDAPAttribute attribute = (LDAPAttribute)allAttributes.next();
                String attributeName = attribute.getName();
                // Save objectclass values
                if (attributeName.equalsIgnoreCase( "objectClass" ) ) {
                            objClass =  attribute.getStringValues();
                }

                // Save the memberQueryURL attribute if present
                else if (attributeName.equalsIgnoreCase( "memberQueryURL" )){
                           queryURL =  attribute.getStringValues();
                }

                // Save the dgIdentity attribute if present
                else if (attributeName.equalsIgnoreCase( "dgIdentity" ) ) {
                           identity =  attribute.getStringValues();
                }

                // Save the excludedMember attribute if present
                else if (attributeName.equalsIgnoreCase( "excludedMember" )) {
                           excludedMember =  attribute.getStringValues();
                }

                /* Save the member attribute.  This may also show up
                 * as uniqueMember
                 */
                else if ( attributeName.equalsIgnoreCase ( "member" ) ||
                          attributeName.equalsIgnoreCase ( "uniqueMember" ) ) {
                           member =  attribute.getStringValues();
                }
            }

            /* Verify that this is a group object  (i.e. objectClass contains
             * the value "group", "groupOfNames", or "groupOfUniqueNames").
             * Also determine if this is a dynamic group object
             * (i.e. objectClass contains the value "dynamicGroup" or
             * "dynamicGroupAux").
             */
            while(objClass.hasMoreElements()) {
                String objectName = (String) objClass.nextElement();
                if ( objectName.equalsIgnoreCase( "group" ) ||
                     objectName.equalsIgnoreCase( "groupOfNames" ) ||
                     objectName.equalsIgnoreCase( "groupOfUniqueNames") )
                     isGroup=true;
                else if ( objectName.equalsIgnoreCase( "dynamicGroup" ) ||
                          objectName.equalsIgnoreCase( "dynamicGroupAux" ) )
                          isGroup = isDynamicGroup = true;
            }

            if (!isGroup) {
                System.out.println("\tThis object is NOT a group object."
                                   + "Exiting.\n");
                System.exit(0);
            }

            /* If this is a dynamic group, display its memberQueryURL, identity
             * and excluded member list.
             */
            if ( isDynamicGroup )  {
                if ( (queryURL != null)&& (queryURL.hasMoreElements()) ) {
                     System.out.println("\tMember Query URL:");
                     while (queryURL.hasMoreElements())
                        System.out.println("\t\t" + queryURL.nextElement());
                }

                if ( (identity != null) && (identity.hasMoreElements()) ) {
                     System.out.println("\tIdentity for search:"
                                       + identity.nextElement());
                }

                if ( (excludedMember != null) &&
                            (excludedMember.hasMoreElements()) ) {
                     System.out.println("\tExcluded member list:");
                     while (excludedMember.hasMoreElements())
                        System.out.println("\t\t"
                                       + excludedMember.nextElement());
                }
            }

            // Print the goup's member list
            System.out.println("\n\tMember list:");
            while ( member.hasMoreElements() )
                System.out.println("\t\t" + member.nextElement());

            // disconnect with the server
            lc.disconnect();
        }
        catch( LDAPException e ) {
            System.out.println( "Error: " + e.toString() );
            System.exit(1);
        }
        catch( UnsupportedEncodingException e ) {
            System.out.println( "Error: " + e.toString() );
        }
        System.exit(0);
    }
}
