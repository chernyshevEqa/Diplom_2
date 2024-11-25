package models.ingredientsPojo;

import java.util.List;

public class IngredientsRequest {
    private boolean success;
    private List<Ingredient> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Ingredient> getData() {
        return data;
    }

    public void setIngredients(List<Ingredient> data) {
        this.data = data;
    }
}
