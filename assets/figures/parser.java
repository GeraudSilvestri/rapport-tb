public static Problem parseProblem(String filename){
    ObjectMapper mapper = new ObjectMapper();
    try {
        return mapper.readValue(new File(filename), Problem.class);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}