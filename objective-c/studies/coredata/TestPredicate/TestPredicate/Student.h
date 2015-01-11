//
//  Student.h
//  TestPredicate
//
//  Created by Mauricio Leal on 1/6/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class School;

@interface Student : NSManagedObject

@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) School *student_belongs_school;

@end
