package kr.supporti;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

public class ItemRecommend {

    public static void main(String[] args) throws IOException, TasteException {
        // 1.파일을 읽고
        DataModel model = new FileDataModel(new File("data/movies.csv"));

        // 2.아이템 기준 유사성
        ItemSimilarity similarity = new LogLikelihoodSimilarity(model);
        TanimotoCoefficientSimilarity similarity1 = new TanimotoCoefficientSimilarity(model);

        // 3.아이템 기준 추천 모델
        GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(model, similarity);
        GenericItemBasedRecommender recommender1 = new GenericItemBasedRecommender(model, similarity1);

        int x = 1;
        for (LongPrimitiveIterator items = model.getItemIDs(); items.hasNext();) {
            long itemId = items.nextLong();
            List<RecommendedItem> recommendations = recommender.mostSimilarItems(itemId, 5);
            System.out.println("type1))");
            for (RecommendedItem recommendation : recommendations) {
                System.out.println(itemId + "," + recommendation.getItemID() + "," + recommendation.getValue());
            }
            List<RecommendedItem> recommendations1 = recommender1.mostSimilarItems(itemId, 5);

            System.out.println("type2))");
            for (RecommendedItem recommendation : recommendations1) {
                System.out.println(itemId + "," + recommendation.getItemID() + "," + recommendation.getValue());
            }
            x++;
            if (x > 10)
                System.exit(1);
        }
    }

}
