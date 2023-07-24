for (String s : salles) {
    for (int i = 0; i < PERIODES; i++) {
        for (Lesson l : problem.getAllLessonsArray()) {
            String nom = "sch_" + i + "_" + l.getLesson_nom() + "_" + s;
            // récupération de la variable liant le cours à la salle
            GRBVar Z = schVars.get(nom);
            if (Z == null) {
                continue;
            }
            GRBLinExpr lin = new GRBLinExpr();

            for (Groupe g : groupes) {
                String nom2 = "grp_" + i + "_" + g.getGroupe_nom() + "_" + l.getLesson_nom();
                // récupération de la variable liant le cours au groupe
                GRBVar Y = grpVars.get(nom2);
                if (Y == null) {
                    continue;
                }
                lin.addTerm(g.getEffectif(), Y); // ajout de l'effectif du groupe
                // si le groupe a le cours, ça fait 1*effectif, sinon 0*effectif
            }

            // ajout de la capacité de la salle
            lin.addTerm(constante - problem.getRoomCapacity(s), Z);
            // si le cours est dans la salle
            //      on ajoute la constante - la capacité de la salle
            // ainsi, si le cours est dans la salle, ça fait
            //      effectif + 10'000 - capacité <= 10'000
            // si le cours n'est pas dans la salle, ça fait
            //      effectif <= 10'000, ce qui est toujours vrais

            String constraintName = "Constr7-Salle-" + l.getLesson_nom() + "_" + s + "_" + i;
            // ajout de la contrainte
            gurobi.addConstr(lin, GRB.LESS_EQUAL, constante, constraintName);
            lin.clear();
        }
    }
}