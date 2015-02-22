//
//  Person.h
//  TestMOC
//
//  Created by Mauricio Leal on 2/21/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface Person : NSManagedObject

@property (nonatomic, retain) NSNumber * iD;
@property (nonatomic, retain) NSString * firstName;
@property (nonatomic, retain) NSString * lastName;

+(NSEntityDescription *)entity:(NSManagedObjectContext *)context;
+(Person *)newPerson:(NSManagedObjectContext *)context;

-(void)parseJSON:(NSData *)data;
-(void)parseJSONfromDictionary:(NSDictionary *)dict;
-(NSData *)toJSON;

@end
