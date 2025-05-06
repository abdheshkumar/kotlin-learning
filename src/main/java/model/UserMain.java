package model;

public class UserMain {
    public static void main(String[] args) {
        final UserBuilder builder = new UserBuilder();

        builder.setFirstName("Abdhesh");
        builder.setLastName("Kumar");

        final TwitterBuilder twitterBuilder = new TwitterBuilder();
        twitterBuilder.setHandle("@abdhesh_rkg");
        builder.setTwitter(twitterBuilder.build());

        final CompanyBuilder companyBuilder = new CompanyBuilder();
        companyBuilder.setName("Expedia");
        companyBuilder.setCity("London");
        builder.setCompany(companyBuilder.build());

        final User user = builder.build();
        System.out.println("Created user is: " + user);
    }

}
