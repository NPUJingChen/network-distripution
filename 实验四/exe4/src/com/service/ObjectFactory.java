
package com.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.service package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Register_QNAME = new QName("http://www.service.com", "register");
    private final static QName _UserExistResponse_QNAME = new QName("http://www.service.com", "userExistResponse");
    private final static QName _AddProjectResponse_QNAME = new QName("http://www.service.com", "addProjectResponse");
    private final static QName _Delete_QNAME = new QName("http://www.service.com", "delete");
    private final static QName _UserExist_QNAME = new QName("http://www.service.com", "userExist");
    private final static QName _SearchProject_QNAME = new QName("http://www.service.com", "searchProject");
    private final static QName _Clear_QNAME = new QName("http://www.service.com", "clear");
    private final static QName _ClearResponse_QNAME = new QName("http://www.service.com", "clearResponse");
    private final static QName _RegisterResponse_QNAME = new QName("http://www.service.com", "registerResponse");
    private final static QName _AddProject_QNAME = new QName("http://www.service.com", "addProject");
    private final static QName _DeleteResponse_QNAME = new QName("http://www.service.com", "deleteResponse");
    private final static QName _SearchProjectResponse_QNAME = new QName("http://www.service.com", "searchProjectResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SearchProjectResponse }
     * 
     */
    public SearchProjectResponse createSearchProjectResponse() {
        return new SearchProjectResponse();
    }

    /**
     * Create an instance of {@link AddProject }
     * 
     */
    public AddProject createAddProject() {
        return new AddProject();
    }

    /**
     * Create an instance of {@link DeleteResponse }
     * 
     */
    public DeleteResponse createDeleteResponse() {
        return new DeleteResponse();
    }

    /**
     * Create an instance of {@link RegisterResponse }
     * 
     */
    public RegisterResponse createRegisterResponse() {
        return new RegisterResponse();
    }

    /**
     * Create an instance of {@link Clear }
     * 
     */
    public Clear createClear() {
        return new Clear();
    }

    /**
     * Create an instance of {@link ClearResponse }
     * 
     */
    public ClearResponse createClearResponse() {
        return new ClearResponse();
    }

    /**
     * Create an instance of {@link SearchProject }
     * 
     */
    public SearchProject createSearchProject() {
        return new SearchProject();
    }

    /**
     * Create an instance of {@link UserExist }
     * 
     */
    public UserExist createUserExist() {
        return new UserExist();
    }

    /**
     * Create an instance of {@link AddProjectResponse }
     * 
     */
    public AddProjectResponse createAddProjectResponse() {
        return new AddProjectResponse();
    }

    /**
     * Create an instance of {@link Delete }
     * 
     */
    public Delete createDelete() {
        return new Delete();
    }

    /**
     * Create an instance of {@link UserExistResponse }
     * 
     */
    public UserExistResponse createUserExistResponse() {
        return new UserExistResponse();
    }

    /**
     * Create an instance of {@link Register }
     * 
     */
    public Register createRegister() {
        return new Register();
    }

    /**
     * Create an instance of {@link Project }
     * 
     */
    public Project createProject() {
        return new Project();
    }

    /**
     * Create an instance of {@link User }
     * 
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Register }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.service.com", name = "register")
    public JAXBElement<Register> createRegister(Register value) {
        return new JAXBElement<Register>(_Register_QNAME, Register.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserExistResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.service.com", name = "userExistResponse")
    public JAXBElement<UserExistResponse> createUserExistResponse(UserExistResponse value) {
        return new JAXBElement<UserExistResponse>(_UserExistResponse_QNAME, UserExistResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddProjectResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.service.com", name = "addProjectResponse")
    public JAXBElement<AddProjectResponse> createAddProjectResponse(AddProjectResponse value) {
        return new JAXBElement<AddProjectResponse>(_AddProjectResponse_QNAME, AddProjectResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Delete }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.service.com", name = "delete")
    public JAXBElement<Delete> createDelete(Delete value) {
        return new JAXBElement<Delete>(_Delete_QNAME, Delete.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserExist }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.service.com", name = "userExist")
    public JAXBElement<UserExist> createUserExist(UserExist value) {
        return new JAXBElement<UserExist>(_UserExist_QNAME, UserExist.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SearchProject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.service.com", name = "searchProject")
    public JAXBElement<SearchProject> createSearchProject(SearchProject value) {
        return new JAXBElement<SearchProject>(_SearchProject_QNAME, SearchProject.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Clear }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.service.com", name = "clear")
    public JAXBElement<Clear> createClear(Clear value) {
        return new JAXBElement<Clear>(_Clear_QNAME, Clear.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClearResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.service.com", name = "clearResponse")
    public JAXBElement<ClearResponse> createClearResponse(ClearResponse value) {
        return new JAXBElement<ClearResponse>(_ClearResponse_QNAME, ClearResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.service.com", name = "registerResponse")
    public JAXBElement<RegisterResponse> createRegisterResponse(RegisterResponse value) {
        return new JAXBElement<RegisterResponse>(_RegisterResponse_QNAME, RegisterResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddProject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.service.com", name = "addProject")
    public JAXBElement<AddProject> createAddProject(AddProject value) {
        return new JAXBElement<AddProject>(_AddProject_QNAME, AddProject.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.service.com", name = "deleteResponse")
    public JAXBElement<DeleteResponse> createDeleteResponse(DeleteResponse value) {
        return new JAXBElement<DeleteResponse>(_DeleteResponse_QNAME, DeleteResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SearchProjectResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.service.com", name = "searchProjectResponse")
    public JAXBElement<SearchProjectResponse> createSearchProjectResponse(SearchProjectResponse value) {
        return new JAXBElement<SearchProjectResponse>(_SearchProjectResponse_QNAME, SearchProjectResponse.class, null, value);
    }

}
