//
//  School.h
//  TestPredicate
//
//  Created by Mauricio Leal on 1/6/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Student;

@interface School : NSManagedObject

@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSSet *school_has_students;
@end

@interface School (CoreDataGeneratedAccessors)

- (void)addSchool_has_studentsObject:(Student *)value;
- (void)removeSchool_has_studentsObject:(Student *)value;
- (void)addSchool_has_students:(NSSet *)values;
- (void)removeSchool_has_students:(NSSet *)values;

@end
