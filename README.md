# jaxrs-client-in-a-nutshell
The original intention of this little project was to get familiar with the JAX-RS client API. The next step was to provide a simple layer of abstraction for using this API: this lightweight framework.

How should a developer interact with the framework? Let's show an example:
```java
URI baseUri = ... // set your base URI here, e. g. "http://resources.company.com"
RestServiceClient restClient = DefaultRestServiceClient.newInstance(baseUri);
restClient.openConversation();

// read a specific resource, here some kind of a generic item identified by its id '24'
restClient.setPath("items/24");
Item item24 = restClient.read(Item.class);

// modify item24 in between... finally updating the modified item
restClient.update(item24);

restClient.closeConversation();
```
There are couple of things to know of "what's behind the curtain". The code above relies on the default `mediaType` of a RestServiceClient which is `application/json`. The class `Item` is a simple JAXB annotated POJO that represents the content of a JAX-RS conversation. There is neither an authentication mechanism nor any explicit exception handling considered.

The current scope of the framework provides methods to
* create a resource (using a `POST` request)
* read a single resource (using a `GET` request)
* read a list of resources (using a `GET` request)
* update a resource (using a `PUT` request)
* delete a resource (using a `DELETE` request)

The list of things which are currently NOT supported is by far longer than the feature list. Still work in progress...
