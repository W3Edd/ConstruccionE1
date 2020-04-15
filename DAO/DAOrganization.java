package DAO;

import Connection.DBConnection;
import IDAO.IDAOrganization;
import Models.Organization;

public class DAOrganization implements IDAOrganization {
    private Organization organization;
    private DBConnection connection = new DBConnection();

    public DAOrganization(Organization organization) {
        this.organization = organization;
    }

    @Override
    public boolean signUp() {
        boolean signedUp = false;
        if (this.organization != null) {
            if (!this.isRegistered()) {
                String query = "INSERT INTO Organization (name, status, idSector) VALUES (?, ?, ?)";
                String[] values = {this.organization.getName(),
                                    this.organization.getStatus(),
                                    this.organization.getIdSector()};
                if(this.connection.sendQuery(query,values)){
                    signedUp = true;
                }
            } else {
                String query = "UPDATE Organization SET status = 1 WHERE nombre = ?";
                String[] values = {this.organization.getName()};
                if (this.connection.sendQuery(query, values)) {
                    signedUp = true;
                }
            }
        }
        return signedUp;
    }

        @Override
        public boolean isRegistered () {
            boolean isRegistered = false;
            String query = "SELECT COUNT (idOrganization) AS TOTAL FROM Organization WHERE name = ?"; //Comentar lo de no repetir names
            String[] values = {this.organization.getName()};
            String[] names = {"TOTAL"};
            if (this.connection.select(query, values, names)[0][0].equals("1")) {
                isRegistered = true;
            }
            return isRegistered;
        }

        @Override
        public boolean delete() {
            boolean deleted = false;
            if (this.organization != null && this.isRegistered()) {
                if (this.isActive()) {
                    String query = "UPDATE Organization SET status = 0 WHERE name = ?";
                    String[] values = {this.organization.getName()};
                    if (this.connection.sendQuery(query, values)) {
                        deleted = true;
                    }
                } else {
                    deleted = true;
                }
            }
            return deleted;
        }

        @Override
        public boolean isActive() {
            boolean isActive = false;
            if (this.organization != null && this.organization.getName() != null &&
                    this.isRegistered()) {
                String query = "SELECT status FROM Organization WHERE name = ?";
                String[] values = {this.organization.getName()};
                String[] names = {"status"};
                isActive = this.connection.select(query, values, names)[0][0].equals("1");
            }
            return isActive;
        }

        @Override
        public boolean reactivate () {
            boolean reactivated = false;
            if (this.organization != null && this.isRegistered()) {
                if (this.isActive()) {
                    String query = "UPDATE Organization SET status = 1 WHERE name = ?";
                    String[] values = {this.organization.getName()};
                    if (this.connection.sendQuery(query, values)) {
                        reactivated = true;
                    }
                } else {
                    reactivated = true;
                }
            }
            return reactivated;
        }
}
