package springconfig;

import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.MongoSimpleTypes;
import org.springframework.data.util.TypeInformation;

import java.util.AbstractMap;

/**
 * Created by senthil
 */
public class GingerMappingContext extends MongoMappingContext {

    @Override
    protected boolean shouldCreatePersistentEntityFor(TypeInformation<?> type) {
        System.out.println(AbstractMap.class.isAssignableFrom(type.getType()));
        boolean res = !MongoSimpleTypes.HOLDER.isSimpleType(type.getType());

        return res;

    }
}
