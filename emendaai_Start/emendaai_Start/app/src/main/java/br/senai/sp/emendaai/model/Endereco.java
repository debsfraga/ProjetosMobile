package br.senai.sp.emendaai.model;

public class Endereco {
        private String cep;
        private String state;
        private String city;
        private String neighborhood;
        private String street;

    @Override
    public String toString() {
        return " Rua: " + street +
                '\n' +" Bairro: " + neighborhood + '\n' +
                " Cidade: " + city + '\n' +
                " UF: " + state + '\n';
    }

    public String getCep() { return cep; }
        public void setCep(String value) { this.cep = value; }

        public String getState() { return state; }
        public void setState(String value) { this.state = value; }

        public String getCity() { return city; }
        public void setCity(String value) { this.city = value; }

        public String getNeighborhood() { return neighborhood; }
        public void setNeighborhood(String value) { this.neighborhood = value; }

        public String getStreet() { return street; }
        public void setStreet(String value) { this.street = value; }
}

