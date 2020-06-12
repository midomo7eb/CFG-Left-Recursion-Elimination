import java.util.Vector;


public class CFG {
    public static Vector<Rule> rules = new Vector<>();

    public static void print() {
        for (Rule r : rules)
            r.printString();
    }

    public static void LRE(String grammar) {
        String[] sp = grammar.split(";");

        for (String r : sp) {
            Rule rule = new Rule(r);
            rules.add(rule);
        }

        for (int i = 0; i < rules.size(); i++) {
            Rule newRule = removeLeftRec(i);
            substituteLeftWithRight(i, newRule);
            
        }
        filterWrong();
    }

    public static Rule removeLeftRec(int pos) {
        String left = rules.get(pos).left;
        Vector<String> rec = new Vector<>();
        Vector<String> nonrec = new Vector<>();

        for (int i = 0; i < rules.get(pos).right.size(); i++) {
            if (rules.get(pos).left.charAt(0) == rules.get(pos).right.get(i).charAt(0))
                rec.add(rules.get(pos).right.get(i));
            else
                nonrec.add(rules.get(pos).right.get(i));
        }
        Rule newRule = new Rule();
        if (rec.size() != 0) {
            rules.get(pos).right = new Vector<>();
            for (String non : nonrec)
                rules.get(pos).right.add(non + rules.get(pos).left + "\'");

            Vector<String> newRec = new Vector<>();
            for (int i = 0; i < rec.size(); i++)
                newRec.add(rec.get(i).substring(1) + left + "\'");

            newRule = new Rule(rules.get(pos).left + "\'", newRec);
        }
        return newRule;


    }
    public static void filterWrong() {
    	String prob = "";
    	for(int i = 0 ;i<rules.size();i++) {
    		
    		if(!((rules.get(i).left.equals(rules.get(i).left.charAt(0)+""))||(rules.get(i).left.equals(rules.get(i).left.charAt(0)+"'")))) {
        		prob = rules.get(i).left.charAt(0)+"";
    			rules.removeElementAt(i);
        		i--;
    		}
    	}
    	for(int i = 0;i<rules.size();i++) {
			Rule update = rules.get(i);
    		if(!prob.equals("")){
    			if(rules.get(i).left.equals(prob+"'")) {
        			update.right.add(prob+prob+"'");
        		}
    		}
    		rules.set(i, update);
    		
    	}
    	for(int i=0;i<rules.size();i++) {
			Rule update = rules.get(i);
    		for(int j = 0;j<rules.get(i).right.size();j++) {
    			update.right.set(j,rules.get(i).right.get(j).replace(update.left.charAt(0)+"''", ""));
    			update.right.set(j,rules.get(i).right.get(j).replace(update.left.charAt(0)+"'''", ""));
    			update.right.set(j,rules.get(i).right.get(j).replace(update.left.charAt(0)+"''''", ""));
    			update.right.set(j,rules.get(i).right.get(j).replace(update.left.charAt(0)+"'''''", ""));

    		}
    		rules.set(i, update);
    	}
    	
    }
    public static void substituteLeftWithRight(int pos, Rule newRule) {
        Vector<Rule> updateRules = new Vector<>();
        for (int i = 0; i < rules.size(); i++) {
            if (i <= pos) {
                updateRules.add(rules.get(i));
                if (i == pos && newRule.left != "")
                    updateRules.add(newRule);
            } else {
                Vector<String> newRight = new Vector<>();
                for (int j = 0; j < rules.get(i).right.size(); j++) {
                    if (rules.get(i).right.get(j).charAt(0) == rules.get(pos).left.charAt(0)) {
                        for (int x = 0; x < rules.get(pos).right.size(); x++) {
                            newRight.add(rules.get(pos).right.get(x) + rules.get(i).right.get(j).substring(1));
                        }
                    } else
                        newRight.add(rules.get(i).right.get(j));

                }
                rules.get(i).right = newRight;
                updateRules.add(rules.get(i));
            }
        }
        rules = updateRules;
    }

    public static void main(String[] args) {
        String grammar = "S,SuS,SS,Ss,lSr,a";
        System.out.println("Grammar with left rec: " + grammar);
        LRE(grammar);

        System.out.print("Grammar after eliminate left rec: ");
	        print();
    }
}
