package com.examples.gson.generaltypes.typeadapters;

import com.examples.gson.generaltypes.Animal;
import com.examples.gson.generaltypes.AnimalEntity;
import com.examples.gson.generaltypes.impl.CatImpl;
import com.examples.gson.generaltypes.impl.DogImpl;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.springframework.util.Assert;

import java.io.IOException;

public class AnimalEntityTypeAdapter extends TypeAdapter<AnimalEntity> {
    private Gson gson;

    public AnimalEntityTypeAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, AnimalEntity value) throws IOException {
        if (value==null){
            out.nullValue();
            return;
        }
        out.beginObject();
        out.name("price").value(value.getPrice());
        out.name("animal");
        if (value.getAnimal() instanceof CatImpl){
            CatImpl cat=(CatImpl)value.getAnimal();
            this.gson.getAdapter(CatImpl.class).write(out, cat);
        }else if (value.getAnimal() instanceof DogImpl){
            DogImpl dog=(DogImpl)value.getAnimal();
            this.gson.getAdapter(DogImpl.class).write(out, dog);
        }
        out.endObject();
    }

    @Override
    public AnimalEntity read(JsonReader in) throws IOException {
        in.beginObject();
        AnimalEntity.AnimalEntityBuilder builder=AnimalEntity.builder();
        while (in.hasNext()){
            /**
             * 因为只有先获取了列表中Animal的子类类型，才能够需要调用哪个adapter继续read，所以在具体的adapter.read中的做法会有些奇怪（缺少in.beginObject()）
             * 这是因为在AnimalTypeAdapter.read中已经执行过in.beginObject了
             */
            switch (in.nextName()){
                case "price": {
                    builder.price(in.nextInt());
                };break;
                case "animal": {
                    in.beginObject();
                    String name =in.nextName();
                    Assert.isTrue("type".equals(name), "第一项必须是type，不然后面的根据类型动态判断子类就无法实现了");
                    String type=in.nextString();//动态获取子类的类型
                    if ("cat".equals(type)){
                        CatImpl cat=this.gson.<CatImpl>getAdapter(CatImpl.class).read(in);
                        cat.setType(type);//在CatTypeAdapter中并不会解析type，所以在AnimalEntityTypeAdapter.read中要对animal的type赋值
                        builder.animal(cat);
                    }else if ("dog".equals(type)){
                        DogImpl dog=this.gson.<DogImpl>getAdapter(DogImpl.class).read(in);
                        dog.setType(type);
                        builder.animal(dog);
                    }else{
                        throw new RuntimeException();
                    }
                    in.endObject();
                }break;
                default:break;
            }
        }

        in.endObject();
        return builder.build();
    }
}
