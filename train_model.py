#ASHIMA SHREE ANISHKA
import os
import numpy as np
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Conv2D, MaxPooling2D, Flatten, Dense, Dropout
from tensorflow.keras.callbacks import EarlyStopping
from sklearn.utils import class_weight

# Path to dataset (must contain 'Healthy/' and 'Diseased/' subfolders)
DATASET_PATH = r'C:\Users\hp\OneDrive\Desktop\Plant Disease App'

# Constants
IMAGE_SIZE = (128, 128)
BATCH_SIZE = 16
EPOCHS = 15

# Image preprocessing
datagen = ImageDataGenerator(
    rescale=1.0/255,
    validation_split=0.2
)

train_gen = datagen.flow_from_directory(
    DATASET_PATH,
    target_size=IMAGE_SIZE,
    batch_size=BATCH_SIZE,
    class_mode='binary',
    subset='training',
    shuffle=True
)

val_gen = datagen.flow_from_directory(
    DATASET_PATH,
    target_size=IMAGE_SIZE,
    batch_size=BATCH_SIZE,
    class_mode='binary',
    subset='validation',
    shuffle=False
)

# Save label mapping
labels_dict = train_gen.class_indices
print("Class indices:", labels_dict)

# Save class names to labels.txt
labels_path = os.path.join(DATASET_PATH, "labels.txt")
with open(labels_path, "w") as f:
    for label in labels_dict:
        f.write(label + "\n")
print(f"✅ Class names saved to: {labels_path}")

# Calculate class weights dynamically
y_train = train_gen.classes
computed_weights = class_weight.compute_class_weight(
    class_weight='balanced',
    classes=np.unique(y_train),
    y=y_train
)
class_weights = dict(enumerate(computed_weights))
print("📊 Computed class weights:", class_weights)

# Build binary classification CNN
model = Sequential([
    Conv2D(32, (3, 3), activation='relu', input_shape=(128, 128, 3)),
    MaxPooling2D(2, 2),

    Conv2D(64, (3, 3), activation='relu'),
    MaxPooling2D(2, 2),

    Flatten(),
    Dense(128, activation='relu'),
    Dropout(0.3),
    Dense(1, activation='sigmoid')
])

model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])

# Train the model
model.fit(
    train_gen,
    validation_data=val_gen,
    epochs=EPOCHS,
    class_weight=class_weights,
    callbacks=[EarlyStopping(monitor='val_loss', patience=3, restore_best_weights=True)]
)

# Save the trained model
model_save_path = os.path.join(DATASET_PATH, "plant_disease_model.h5")
model.save(model_save_path)
print(f"✅ Model trained and saved successfully at: {model_save_path}")