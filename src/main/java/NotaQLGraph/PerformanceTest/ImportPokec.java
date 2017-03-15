package NotaQLGraph.PerformanceTest;

import NotaQLGraph.Database.Database;
import NotaQLGraph.Database.Neo4JDatabase;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j2.Neo4j2Graph;
import com.tinkerpop.blueprints.util.wrappers.id.IdGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * Created by yannick on 17.02.16.
 */
public class ImportPokec {
    public static void main (String[] args){
        //Database source = new Neo4JDatabase("/media/yannick/BE2EE9682EE91A63/Master/pokec2.db");
        Database source = new Neo4JDatabase("/media/yannick/32gb/Yannick/Pokec.db");
        BufferedReader reader = null;


        try {
            File file = new File("/home/yannick/Downloads/neodata/profil.txt");
            reader = new BufferedReader(new FileReader(file));
            LinkedList<String> header = new LinkedList<String>();
            header.add("_key");
            header.add("public");
            header.add("completion_percentage");
            header.add("gender");
            header.add("region");
            header.add("last_login");
            header.add("registration");
            header.add("AGE");
            header.add("body");
            header.add("I_am_working_in_field");
            header.add("spoken_languages");
            header.add("hobbies");
            header.add("I_most_enjoy_good_food");
            header.add("pets");
            header.add("body_type");
            header.add("my_eyesight");
            header.add("eye_color");
            header.add("hair_color");
            header.add("hair_type");
            header.add("completed_level_of_education");
            header.add("favourite_color");
            header.add("relation_to_smoking");
            header.add("relation_to_alcohol");
            header.add("sign_in_zodiac");
            header.add("on_pokec_i_am_looking_for");
            header.add("love_is_for_me");
            header.add("relation_to_casual_sex");
            header.add("my_partner_should_be");
            header.add("marital_status");
            header.add("children");
            header.add("relation_to_children");
            header.add("I_like_movies");
            header.add("I_like_watching_movie");
            header.add("I_like_music");
            header.add("I_mostly_like_listening_to_music");
            header.add("the_idea_of_good_evening");
            header.add("I_like_specialties_from_kitchen");
            header.add("fun");
            header.add("I_am_going_to_concerts");
            header.add("my_active_sports");
            header.add("my_passive_sports");
            header.add("profession");
            header.add("I_like_books");
            header.add("life_style");
            header.add("music");
            header.add("cars");
            header.add("politics");
            header.add("relationships");
            header.add("art_culture");
            header.add("hobbies_interests");
            header.add("science_technologies");
            header.add("computers_internet");
            header.add("education");
            header.add("sport");
            header.add("movies");
            header.add("travelling");
            header.add("health");
            header.add("companies_brands");
            header.add("more");
            String line;
            Graph g = source.getDatabase();
            int anzahl=0;
          knoten:  while ((line = reader.readLine()) != null) {

                if (anzahl%1000==0)System.out.println(anzahl);
                if (anzahl%10000==0){
                   System.out.println("vor commit");
                    ((IdGraph)g).commit();
                    System.out.println("nach commit");

                }
                StringTokenizer st = new StringTokenizer(line,"\t");
                int counter = 0;
                Vertex v=null;
                String value;
                anzahl++;
                while (st.hasMoreTokens()) {
                    value =  st.nextToken();
                    if (counter==0){
                     //   if(Integer.parseInt(value)>250000) {continue knoten;}else{anzahl++;}
                        v = g.addVertex(value);
                //        array.put(value,v);
                   }
                   else if (!(value.equals("null"))){
                       v.setProperty(header.get(counter),value);
                   }
                    counter++;

                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        //import edges
        reader = null;
        try {
            File file = new File("/home/yannick/Downloads/neodata/relation.txt");
            reader = new BufferedReader(new FileReader(file));

            String line;
            Graph g = source.getDatabase();
            int anzahl=0;
            while ((line = reader.readLine()) != null) {

                if (anzahl%10000==0){
                    System.out.println(anzahl);

                }
                if (anzahl%100000==0){
                    ((IdGraph)g).commit();

                }
                StringTokenizer st = new StringTokenizer(line,"\t");

                Vertex v=null;
                String value;
                String value2;

                value =  st.nextToken();
                value2 = st.nextToken();
            //    if(Integer.parseInt(value)>250000||Integer.parseInt(value2)>250000) {continue;}else {anzahl++;}
                //array.get(value).addEdge("kennt",array.get(value2));
                g.getVertex(value).addEdge("kennt",g.getVertex(value2));
                anzahl++;






































            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        source.getDatabase().shutdown();
    }
}
