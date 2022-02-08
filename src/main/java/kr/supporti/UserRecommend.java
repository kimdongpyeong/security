package kr.supporti;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class UserRecommend {

    public static void main(String[] args) throws IOException, TasteException {
        // 1.파일을 읽고
        DataModel model = new FileDataModel(new File("data/movies.csv"));

        // 2.유저 기준 유사성
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);

        // 3. 0.1보다 큰 유사성을 가진 모든것을 사용 및 ThresholdUserNeighborhood을 통해 구현
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
        UserNeighborhood neighborhood1 = new NearestNUserNeighborhood(2, similarity, model);

        // 4.유저 기준 추천 모델
        UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
        UserBasedRecommender recommender1 = new GenericUserBasedRecommender(model, neighborhood1, similarity);

        long[] neighbors = neighborhood.getUserNeighborhood(2);
        System.out.println("2번의 이웃은?");
        for (long user : neighbors) {
            System.out.print(user + " ");
        }
        long[] neighbors1 = neighborhood1.getUserNeighborhood(2);
        System.out.println("\ntype1) 2번의 이웃은?");
        for (long user : neighbors1) {
            System.out.print(user + " ");
        }
        // 5.(A, B)->A번 유저에게 B개의 아이템 추천
        List<RecommendedItem> recommendations = recommender.recommend(2, 3);
        for (RecommendedItem recommendation : recommendations) {
            System.out.println(recommendation);
        }
        List<RecommendedItem> recommendations1 = recommender1.recommend(1, 3);
        for (RecommendedItem recommendation : recommendations1) {
            System.out.println(recommendation);
        }
    }

}
